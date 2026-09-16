package io.github.thesaint14.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import io.github.thesaint14.exception.EmptyFileException;
import io.github.thesaint14.exception.MalformedRowException;

public class DirectoryIngestorTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private void writeFile(String name, String... lines) throws IOException {
        Files.write(tempFolder.newFile(name).toPath(), List.of(lines));
    }

    private IngestResult findByFileName(List<IngestResult> results, String name) {
        return results.stream()
            .filter(r -> r.getFile().getFileName().toString().equals(name))
            .findFirst()
            .orElseThrow();
    }

    @Test
    public void mixOfGoodAndBadFilesReturnsResultsForEveryFile() throws IOException {
        writeFile("good1.csv", "a,b", "1,2");
        writeFile("good2.csv", "x,y", "3,4");
        writeFile("empty.csv"); // zero lines -> EmptyFileException
        writeFile("ragged.csv", "a,b,c", "1,2,3", "4,5,6,7"); // extra column on row 3

        List<IngestResult> results = new DirectoryIngestor().ingest(tempFolder.getRoot().toPath());

        assertEquals(4, results.size());
        assertEquals(2, results.stream().filter(IngestResult::isSuccess).count());
        assertEquals(2, results.stream().filter(r -> !r.isSuccess()).count());

        assertTrue(findByFileName(results, "good1.csv").isSuccess());
        assertTrue(findByFileName(results, "good2.csv").isSuccess());

        IngestResult empty = findByFileName(results, "empty.csv");
        assertTrue(empty.getError() instanceof EmptyFileException);

        IngestResult ragged = findByFileName(results, "ragged.csv");
        assertTrue(ragged.getError() instanceof MalformedRowException);
        MalformedRowException raggedError = (MalformedRowException) ragged.getError();
        assertEquals(3, raggedError.getRowNumber());
        assertEquals(3, raggedError.getExpectedColumns());
        assertEquals(4, raggedError.getActualColumns());
    }

    @Test
    public void nonMatchingExtensionsAreExcludedEntirely() throws IOException {
        writeFile("data.csv", "a,b", "1,2");
        writeFile("notes.json", "{}");

        List<IngestResult> results = new DirectoryIngestor().ingest(tempFolder.getRoot().toPath());

        assertEquals(1, results.size());
        assertEquals("data.csv", results.get(0).getFile().getFileName().toString());
    }

    @Test
    public void emptyDirectoryReturnsEmptyList() throws IOException {
        List<IngestResult> results = new DirectoryIngestor().ingest(tempFolder.getRoot().toPath());

        assertTrue(results.isEmpty());
    }

    @Test
    public void handlesMoreFilesThanThePoolSize() throws IOException {
        for (int i = 0; i < 6; i++) {
            writeFile("f" + i + ".csv", "a,b", i + ",0");
        }

        List<IngestResult> results = new DirectoryIngestor().ingest(tempFolder.getRoot().toPath());

        assertEquals(6, results.size());
        assertTrue(results.stream().allMatch(IngestResult::isSuccess));
    }
}