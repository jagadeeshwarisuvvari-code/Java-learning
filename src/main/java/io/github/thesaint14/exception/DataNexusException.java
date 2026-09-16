package io.github.thesaint14.exception;

import lombok.Getter;

public abstract class DataNexusException extends RuntimeException{
    @Getter private final String filePath;

    protected DataNexusException(String message, String filePath){
        super(message);
        this.filePath = filePath;
    }
}
