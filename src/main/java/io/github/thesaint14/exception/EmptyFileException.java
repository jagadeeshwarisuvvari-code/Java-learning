package io.github.thesaint14.exception;

public class EmptyFileException extends DataNexusException {
    public EmptyFileException(String filePath) {
        super("The file is empty: " + filePath, filePath);
    }
    
}
