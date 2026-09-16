package io.github.thesaint14.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.thesaint14.exception.EmptyFileException;
import io.github.thesaint14.exception.MalformedRowException;
import io.github.thesaint14.exception.UnsupportedFileTypeException;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.model.Record;

public class CsvParserTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File writeFile(String name, String... lines) throws IOException {
        File file = tempFolder.newFile(name);
        Files.write(file.toPath(), List.of(lines));
        return file;
    }

    @Test
    public void wellFormedCsvParsesIntoMatchingSchemaAndRecords() throws IOException {
        File file = writeFile("clean.csv", "name,age", "Alice,30", "Bob,25");

        ParsedData result = new CsvParser().parse(file.getPath());

        assertEquals(List.of("name", "age"),
            result.getSchema().getFields().stream().map(f -> f.getName()).toList());
        assertTrue(result.getSchema().getFields().stream().allMatch(f -> f.getType() == FieldType.STRING));
        assertEquals(2, result.getRecords().size());
        assertEquals("Alice", result.getRecords().get(0).getValue("name"));
        assertEquals("30", result.getRecords().get(0).getValue("age"));
    }

    @Test
    public void shortRowPadsMissingFieldsInsteadOfThrowing() throws IOException {
        File file = writeFile("short.csv", "a,b,c", "1,2");

        ParsedData result = new CsvParser().parse(file.getPath());

        Record record = result.getRecords().get(0);
        assertEquals("1", record.getValue("a"));
        assertEquals("2", record.getValue("b"));
        assertEquals(null, record.getValue("c"));
        assertTrue(record.getPaddedFields().contains("c"));
    }

    @Test
    public void longRowThrowsMalformedRowExceptionWithRowAndColumnCounts() throws IOException {
        File file = writeFile("ragged.csv", "a,b,c", "1,2,3", "4,5,6,7");

        try {
            new CsvParser().parse(file.getPath());
            fail("expected MalformedRowException");
        } catch (MalformedRowException e) {
            assertEquals(file.getPath(), e.getFilePath());
            assertEquals(3, e.getRowNumber());
            assertEquals(3, e.getExpectedColumns());
            assertEquals(4, e.getActualColumns());
        }
    }

    @Test
    public void emptyFileThrowsEmptyFileException() throws IOException {
        File file = tempFolder.newFile("empty.csv");

        try {
            new CsvParser().parse(file.getPath());
            fail("expected EmptyFileException");
        } catch (EmptyFileException e) {
            assertEquals(file.getPath(), e.getFilePath());
        }
    }

    @Test
    public void unsupportedExtensionThrowsBeforeTouchingTheFilesystem() throws IOException {
        String fakePath = "this/path/does/not/exist.xyz";

        try {
            new CsvParser().parse(fakePath);
            fail("expected UnsupportedFileTypeException");
        } catch (UnsupportedFileTypeException e) {
            assertEquals(fakePath, e.getFilePath());
        }
    }
}