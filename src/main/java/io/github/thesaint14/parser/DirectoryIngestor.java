package io.github.thesaint14.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import io.github.thesaint14.exception.DataNexusException;

public class DirectoryIngestor {

    private static final int MAX_CONCURRENT_FILES = 5;

    public List<IngestResult> ingest(Path directory) throws IOException {
        List<Path> files = listMatchingFiles(directory);
        if (files.isEmpty()) {
            return List.of();
        }

        ExecutorService pool = Executors.newFixedThreadPool(Math.min(MAX_CONCURRENT_FILES, files.size()));
        try {
            List<Future<IngestResult>> futures = new ArrayList<>();
            for (Path file : files) {
                futures.add(pool.submit(() -> parseOne(file)));
            }

            List<IngestResult> results = new ArrayList<>();
            for (Future<IngestResult> future : futures) {
                results.add(future.get());
            }
            return results;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Directory ingestion interrupted", e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Unexpected failure during ingestion", e);
        } finally {
            pool.shutdown();
        }
    }

    private IngestResult parseOne(Path file) {
        try {
            ParsedData data = new CsvParser().parse(file.toString());
            return IngestResult.success(file, data);
        } catch (DataNexusException | IOException e) {
            return IngestResult.failure(file, e);
        }
    }

    private List<Path> listMatchingFiles(Path directory) throws IOException {
        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(p -> {
                    String name = p.toString().toLowerCase();
                    return name.endsWith(".csv") || name.endsWith(".tsv") || name.endsWith(".txt");
                })
                .toList();
        }
    }
}