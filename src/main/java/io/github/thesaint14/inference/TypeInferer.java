package io.github.thesaint14.inference;

import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;
import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.parser.ParsedData;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.EnumSet;
import java.util.Set;


public class TypeInferer {

    //tryna check for month, numbers, short forms allat stuff, gl understanding!
    private static final Map<String, Integer> MONTH_LOOKUP = new HashMap<>();
    static {
        String[][] months = {
            {"jan", "january"}, {"feb", "february"}, {"mar", "march"},
            {"apr", "april"}, {"may"}, {"jun", "june"},
            {"jul", "july"}, {"aug", "august"}, {"sep", "september"},
            {"oct", "october"}, {"nov", "november"}, {"dec", "december"}
        };
        for (int i =0; i< months.length; i++) {
            for (String name : months[i]) {
                MONTH_LOOKUP.put(name, i + 1);
            }
        }
    }

    //welcome to my idea of stripping values to get a date identifier, jus pray it work gng TT
    private static final String[] DATE_DELIMITERS = {"-", "/", " ",};

    // only these three actually form a widening ladder; any other type
    // clash (bool vs number, date vs bool, etc.) can't be reconciled and must fall to STRING
    private static final Set<FieldType> NUMERIC_TYPES = EnumSet.of(FieldType.INTEGER, FieldType.LONG, FieldType.DOUBLE);

    private enum DateOrder {UNKNOWN, DAY_FIRST, MONTH_FIRST}
    
    
    //PUBLIC STARTS HERE BBY

    public ParsedData infer(ParsedData parsedData) {
        Schema schema = parsedData.getSchema();
        List<Record> records = parsedData.getRecords();
        List<Field> updatedFields = new ArrayList<>();

        for (Field field : schema.getFields()) {
            ColumnAnalysis analysis = analyzeColumn(field.getName(), records);
            updatedFields.add(new Field(field.getName(), analysis.finalType));

            if (analysis.finalType != FieldType.STRING) {
                convertColumn(field.getName(), records, analysis);
            }
        }

        Schema inferredSchema = new Schema(schema.getName(), updatedFields);
        return new ParsedData(inferredSchema, records);
    }

    //2 passes in total, ts is pass 1, just figuring what types where, get it cuhh
    private ColumnAnalysis analyzeColumn(String fieldName, List<Record> records) {
        FieldType currentType = null; // nothing seen yet - not the same as a real INTEGER value
        boolean allZeroOrOne = true;
        boolean sawZero = false;
        boolean sawOne = false;
        boolean anyValueSeen = false;
        DateOrder dateOrder = DateOrder.UNKNOWN;

        for (Record record : records) {
            if (record.getPaddedFields().contains(fieldName)) continue; // synthetic, no signal

            Object rawObj = record.getValue(fieldName);
            if (rawObj == null) continue;
            String value = rawObj.toString();
            if (value.isEmpty()) continue; // legit empty, no signal, doesn't force STRING

            anyValueSeen = true;
            FieldType detected = classify(value);
            currentType = (currentType == null) ? detected : wider(currentType, detected);

            if (value.equals("0")) sawZero = true;
            else if (value.equals("1")) sawOne = true;
            else allZeroOrOne = false;

            if (detected == FieldType.DATE) {
                dateOrder = refineDateOrder(value, dateOrder);
            }

            if (currentType == FieldType.STRING) break; // can't widen further, stop scanning
        }

        if (currentType == null) currentType = FieldType.INTEGER; // column had no data at all - nothing to infer, keep the old default

        if (currentType == FieldType.INTEGER && anyValueSeen && allZeroOrOne && sawZero && sawOne) {
            currentType = FieldType.BOOLEAN;
        }

        return new ColumnAnalysis(currentType, dateOrder);
    }

    
    //welcome to try and error, yes basic bishh stuff but it is what it is(insert meme here)
    
    
    private FieldType classify(String value) {
        if (isInteger(value)) return FieldType.INTEGER;
        if (isLong(value)) return FieldType.LONG;
        if (isDouble(value)) return FieldType.DOUBLE;
        if (isBoolean(value)) return FieldType.BOOLEAN;
        if (isDateCandidate(value)) return FieldType.DATE;
        return FieldType.STRING;
    }

    private FieldType wider(FieldType a, FieldType b) {
        if (a == b) return a;
        if (NUMERIC_TYPES.contains(a) && NUMERIC_TYPES.contains(b)) {
            return a.ordinal() >= b.ordinal() ? a : b; // INTEGER < LONG < DOUBLE
        }
        return FieldType.STRING; // cross-category contradiction, e.g. a number then a bool/date literal
    }

    private boolean isInteger(String value) {
        try { Integer.parseInt(value); return true; } catch (NumberFormatException e) { return false; }
    }

    private boolean isLong(String value) {
        try { Long.parseLong(value); return true; } catch (NumberFormatException e) { return false; }
    }

    private boolean isDouble(String value) {
        try { Double.parseDouble(value); return true; } catch (NumberFormatException e) { return false; }
    }

    private boolean isBoolean(String value) {
        String lower = value.toLowerCase();
        return lower.equals("true") || lower.equals("false");
    }

    //remember the date strip thingy, here it comes

    private boolean isDateCandidate(String value) {
        return splitDateParts(value) != null;
    }

    //delimt in priority nothin fancy, split into 3 parts if availiable and then storing
    
    private String[] splitDateParts(String value) {
        for (String delimiter : DATE_DELIMITERS) {
            if (value.contains(delimiter)) {
                String regex = delimiter.equals(" ") ? "\\s+" : Pattern.quote(delimiter);
                String[] parts = value.split(regex);
                if (parts.length == 3 && isValidDateParts(parts)) return parts;
            }
        }
        return null;
    }

    private boolean isValidDateParts(String[] parts) {
        int numericParts = 0;
        boolean hasMonthName = false;
        for (String part : parts) {
            String stripped = stripOrdinalSuffix(part);
            if (MONTH_LOOKUP.containsKey(stripped.toLowerCase())) hasMonthName = true;
            else if (isPlainInteger(stripped)) numericParts++;
            else return false; // neither a number nor a known month name
        }
        return (hasMonthName && numericParts == 2) || (!hasMonthName && numericParts == 3);
    }

    //we tryna replicate the bool logic of cycling the whole column to get the base syntax for month and day poistioning down, its easy just read


    private DateOrder refineDateOrder(String value, DateOrder currentGuess) {
        if (currentGuess != DateOrder.UNKNOWN) return currentGuess;

        String[] parts = splitDateParts(value);
        if (parts == null) return currentGuess;

        for (String part : parts) {
            if (MONTH_LOOKUP.containsKey(stripOrdinalSuffix(part).toLowerCase())) {
                return DateOrder.DAY_FIRST; // month is named -> no numeric ambiguity left to resolve
            }
        }

        int first = Integer.parseInt(stripOrdinalSuffix(parts[0]));
        int second = Integer.parseInt(stripOrdinalSuffix(parts[1]));
        if (first > 12) return DateOrder.DAY_FIRST;
        if (second > 12) return DateOrder.MONTH_FIRST;
        return DateOrder.UNKNOWN; // still ambiguous (e.g. "05-06-2026") - keep scanning
    }

    private String stripOrdinalSuffix(String part) {
        return part.replaceAll("(?i)(st|nd|rd|th)$", "");
    }

    private boolean isPlainInteger(String value) {
        try { Integer.parseInt(value); return true; } catch (NumberFormatException e) { return false; }
    }


    //pass 2, get ready conversion to identified types, gotit??

    private void convertColumn(String fieldName, List<Record> records, ColumnAnalysis analysis) {
        for (Record record : records) {
            if (record.getPaddedFields().contains(fieldName)) continue; // leave synthetic nulls alone

            Object rawObj = record.getValue(fieldName);
            if (rawObj == null) continue;
            String value = rawObj.toString();
            if (value.isEmpty()) continue; // leave legitimate empties as empty strings

            Object converted = convertValue(value, analysis.finalType, analysis.dateOrder);
            record.getValues().put(fieldName, converted);
        }
    }

    private Object convertValue(String value, FieldType type, DateOrder dateOrder) {
        switch (type) {
            case INTEGER: return Integer.parseInt(value);
            case LONG: return Long.parseLong(value);
            case DOUBLE: return Double.parseDouble(value);
            case BOOLEAN: return parseBoolean(value);
            case DATE: return parseDate(value, dateOrder);
            default: return value; // STRING - no conversion
        }
    }

    private Boolean parseBoolean(String value) {
        String lower = value.toLowerCase();
        if (lower.equals("true")) return Boolean.TRUE;
        if (lower.equals("false")) return Boolean.FALSE;
        return value.equals("1"); // column was upgraded via the all-0/1 rule
    }

    private LocalDate parseDate(String value, DateOrder dateOrder) {
        String[] parts = splitDateParts(value);

        Integer monthFromName = null;
        List<String> numericParts = new ArrayList<>();
        for (String raw : parts) {
            String p = stripOrdinalSuffix(raw);
            Integer m = MONTH_LOOKUP.get(p.toLowerCase());
            if (m != null) monthFromName = m;
            else numericParts.add(p);
        }

        if (monthFromName != null) {
            // One part named the month; of the other two, the 4-digit (or >31) one is the year
            int n0 = Integer.parseInt(numericParts.get(0));
            int day, year;
            if (numericParts.get(0).length() == 4 || n0 > 31) {
                year = n0;
                day = Integer.parseInt(numericParts.get(1));
            } else {
                day = n0;
                year = Integer.parseInt(numericParts.get(1));
            }
            return LocalDate.of(year, monthFromName, day);
        }

    //the datelogic, if all 3 parts are numeric then order by isolating the year format

    int year = 0;
        List<Integer> remaining = new ArrayList<>();
        for (String p : numericParts) {
            int n = Integer.parseInt(p);
            if (p.length() == 4 || n > 31) year = n;
            else remaining.add(n);
        }

        int first = remaining.get(0);
        int second = remaining.get(1);
        int day, month;
        if (dateOrder == DateOrder.MONTH_FIRST) {
            month = first; day = second;
        } else {
            // DAY_FIRST, or still UNKNOWN then we thro to defualt to regular day then mm then yyyy (indi format)
            day = first; month = second;
        }
        return LocalDate.of(year, month, day);
    }

    //column analysis holder? lets call it that

    private static class ColumnAnalysis {
        final FieldType finalType;
        final DateOrder dateOrder;

        ColumnAnalysis(FieldType finalType, DateOrder dateOrder) {
            this.finalType = finalType;
            this.dateOrder = dateOrder;
        }
    }

}
