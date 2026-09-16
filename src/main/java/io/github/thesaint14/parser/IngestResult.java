package io.github.thesaint14.parser;

import java.nio.file.Path;

import lombok.Getter;

@Getter 

public class IngestResult {
    private final Path file;
    private final ParsedData data;
    private final Exception error;

    private IngestResult(Path file, ParsedData data, Exception error) {
        this.file = file;
        this.data = data;
        this.error = error;
    }

    public static IngestResult success(Path file, ParsedData data) {
        return new IngestResult(file, data, null);
    }

    public static IngestResult failure(Path file, Exception error) {
        return new IngestResult(file, null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

}
