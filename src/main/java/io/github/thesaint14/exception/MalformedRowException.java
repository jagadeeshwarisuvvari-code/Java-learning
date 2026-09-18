package io.github.thesaint14.exception;

import lombok.Getter;

public class MalformedRowException extends DataNexusException {
    @Getter private final int rowNumber;
    @Getter private final int expectedColumns;
    @Getter private final int actualColumns;

    public MalformedRowException(String filePath, int rowNumber, int expectedColumns, int actualColumns) {
        super("Invalid structure at row " + rowNumber + " in " + filePath
            + ": expected " + expectedColumns + " columns, but found " + actualColumns, filePath);
        this.rowNumber = rowNumber;
        this.expectedColumns = expectedColumns;
        this.actualColumns = actualColumns;
    }

}
