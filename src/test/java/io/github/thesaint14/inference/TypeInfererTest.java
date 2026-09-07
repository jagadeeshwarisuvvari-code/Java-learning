package io.github.thesaint14.inference;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;
import io.github.thesaint14.parser.ParsedData;

public class TypeInfererTest {

    // ---- helpers ----

    private Record row(Object... kv) {
        Map<String, Object> values = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            values.put((String) kv[i], kv[i + 1]);
        }
        return new Record(values, Collections.emptySet());
    }

    private Record paddedRow(Map<String, Object> values, String... paddedFields) {
        return new Record(values, new HashSet<>(Arrays.asList(paddedFields)));
    }

    private ParsedData parsedData(List<String> columnNames, List<Record> records) {
        List<Field> fields = new java.util.ArrayList<>();
        for (String name : columnNames) fields.add(new Field(name, FieldType.STRING));
        Schema schema = new Schema("test", fields);
        return new ParsedData(schema, records);
    }

    private FieldType typeOf(ParsedData pd, String field) {
        return pd.getSchema().getFields().stream()
            .filter(f -> f.getName().equals(field))
            .findFirst().orElseThrow().getType();
    }

    // ---- numeric widening ----

    @Test
    public void plainIntegersStayInteger() {
        ParsedData pd = parsedData(List.of("n"), List.of(row("n", "1"), row("n", "2"), row("n", "3")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.INTEGER, typeOf(out, "n"));
        assertEquals(Integer.valueOf(2), out.getRecords().get(1).getValue("n"));
    }

    @Test
    public void intWidensToDoubleOnDecimal() {
        ParsedData pd = parsedData(List.of("n"), List.of(row("n", "1"), row("n", "2.5")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.DOUBLE, typeOf(out, "n"));
        assertEquals(Double.valueOf(1.0), out.getRecords().get(0).getValue("n"));
        assertEquals(Double.valueOf(2.5), out.getRecords().get(1).getValue("n"));
    }

    @Test
    public void intWidensToLongOnOverflow() {
        ParsedData pd = parsedData(List.of("n"), List.of(row("n", "5"), row("n", "10000000000")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.LONG, typeOf(out, "n"));
        assertEquals(Long.valueOf(10000000000L), out.getRecords().get(1).getValue("n"));
    }

    // ---- boolean cascade / 0-1 override ----

    @Test
    public void trueFalseLiteralsDetectedDirectly() {
        ParsedData pd = parsedData(List.of("b"), List.of(row("b", "true"), row("b", "false")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.BOOLEAN, typeOf(out, "b"));
        assertEquals(Boolean.TRUE, out.getRecords().get(0).getValue("b"));
    }

    @Test
    public void zeroOneColumnUpgradesToBoolean() {
        ParsedData pd = parsedData(List.of("b"), List.of(row("b", "0"), row("b", "1"), row("b", "0")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.BOOLEAN, typeOf(out, "b"));
        assertEquals(Boolean.FALSE, out.getRecords().get(0).getValue("b"));
        assertEquals(Boolean.TRUE, out.getRecords().get(1).getValue("b"));
    }

    @Test
    public void allOnesColumnStaysIntegerDoesNotUpgrade() {
        // documented edge case: no "0" ever appears, so there's nothing to
        // disambiguate a boolean flag from a genuine count column of 1s.
        ParsedData pd = parsedData(List.of("n"), List.of(row("n", "1"), row("n", "1"), row("n", "1")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.INTEGER, typeOf(out, "n"));
        assertEquals(Integer.valueOf(1), out.getRecords().get(0).getValue("n"));
    }

    // ---- padded / empty handling ----

    @Test
    public void paddedAndEmptyValuesDontParticipateOrGetConverted() {
        Map<String, Object> r1 = new HashMap<>(); r1.put("n", "1");
        Map<String, Object> r2 = new HashMap<>(); r2.put("n", "");      // legit empty
        Map<String, Object> r3 = new HashMap<>(); r3.put("n", null);    // padded/synthetic
        ParsedData pd = parsedData(List.of("n"), List.of(
            paddedRow(r1),
            paddedRow(r2),
            paddedRow(r3, "n")
        ));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.INTEGER, typeOf(out, "n"));
        assertEquals(Integer.valueOf(1), out.getRecords().get(0).getValue("n"));
        assertEquals("", out.getRecords().get(1).getValue("n"));   // untouched
        assertEquals(null, out.getRecords().get(2).getValue("n")); // untouched
    }

    // ---- dates ----

    @Test
    public void unambiguousDayFirstDate() {
        // 13 can't be a month, so the column resolves to day-first immediately
        ParsedData pd = parsedData(List.of("d"), List.of(row("d", "13-06-2026")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.DATE, typeOf(out, "d"));
        assertEquals(LocalDate.of(2026, 6, 13), out.getRecords().get(0).getValue("d"));
    }

    @Test
    public void monthNameDateParsesRegardlessOfOrder() {
        ParsedData pd = parsedData(List.of("d"), List.of(row("d", "15 Jun 2026")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.DATE, typeOf(out, "d"));
        assertEquals(LocalDate.of(2026, 6, 15), out.getRecords().get(0).getValue("d"));
    }

    @Test
    public void neverDisambiguatedColumnDefaultsToDayFirst() {
        // every row is ambiguous (both parts <= 12), so the column never
        // resolves and falls back to day-first per the documented default.
        ParsedData pd = parsedData(List.of("d"), List.of(
            row("d", "05-06-2026"), row("d", "07-08-2026")
        ));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.DATE, typeOf(out, "d"));
        assertEquals(LocalDate.of(2026, 6, 5), out.getRecords().get(0).getValue("d"));
        assertEquals(LocalDate.of(2026, 8, 7), out.getRecords().get(1).getValue("d"));
    }

    @Test
    public void oneDisambiguatingRowFixesOrderForWholeColumn() {
        // first row is ambiguous, second row's "13" can't be a month -> day-first,
        // and that resolution must retroactively apply when row 1 gets converted.
        ParsedData pd = parsedData(List.of("d"), List.of(
            row("d", "05-06-2026"), row("d", "13-06-2026")
        ));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(LocalDate.of(2026, 6, 5), out.getRecords().get(0).getValue("d"));
        assertEquals(LocalDate.of(2026, 6, 13), out.getRecords().get(1).getValue("d"));
    }

    // ---- fallback ----

    @Test
    public void unparseableValuesStayString() {
        ParsedData pd = parsedData(List.of("s"), List.of(row("s", "hello"), row("s", "world")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.STRING, typeOf(out, "s"));
        assertEquals("hello", out.getRecords().get(0).getValue("s"));
    }

    // ---- cross-category contradictions must fall to STRING, never ordinal-promote ----

    @Test
    public void numberThenBooleanLiteralFallsBackToStringNotBoolean() {
        // "2.5" is a real double; "true" only looks boolean in isolation.
        // The column as a whole is neither - it must not get silently declared BOOLEAN.
        ParsedData pd = parsedData(List.of("x"), List.of(row("x", "2.5"), row("x", "true")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.STRING, typeOf(out, "x"));
        assertEquals("2.5", out.getRecords().get(0).getValue("x"));
        assertEquals("true", out.getRecords().get(1).getValue("x"));
    }

    @Test
    public void numberThenDateFallsBackToStringInsteadOfCrashing() {
        // "2026" alone isn't a date candidate (no delimiter); a naive ordinal
        // promotion to DATE would later NPE in parseDate trying to split it.
        ParsedData pd = parsedData(List.of("x"), List.of(row("x", "2026"), row("x", "13-06-2026")));
        ParsedData out = new TypeInferer().infer(pd);
        assertEquals(FieldType.STRING, typeOf(out, "x"));
        assertEquals("2026", out.getRecords().get(0).getValue("x"));
    }
}
