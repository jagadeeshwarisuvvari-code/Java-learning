package io.github.thesaint14.parser;

import java.io.IOException;
import java.util.List;

import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;

public class CsvParser {

    public ParsedData parse(String filePath) throws IOException {
        // TODO
        return null;
    }

    private void verifyStructure(List<String> lines, String delimiter) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }   

        int expectedColumnCount = lines.get(0).split(delimiter).length;
        for (int i = 1; i< lines.size(); i++) {
            String[] values = lines.get(i).split(delimiter);
            if (values.length != expectedColumnCount) {
                throw new IllegalArgumentException(
                    "Invalid structure at row" + (i+1) + ": expected " + expectedColumnCount + " columns, but found " + values.length
                );
                  

            }
        }
    }

    private Schema buildSchema(String header, String delimiter) {
        // TODO
        return null;
    }

    private List<Record> buildRecords(List<String> lines, Schema schema, String delimiter) {
        // TODO
        return null;
    }
}