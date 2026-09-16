package io.github.thesaint14.exception;

public class UnsupportedFileTypeException extends DataNexusException {
    public UnsupportedFileTypeException(String filePath) {
        super("Unsupported file type: " + filePath + " (only .csv, .tsv, .txt supported)", filePath);
    }    
}
