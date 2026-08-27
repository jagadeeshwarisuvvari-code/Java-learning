package io.github.thesaint14.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;

public class CsvParser {

    public ParsedData parse(String filePath) throws IOException {
        String delimiter;
        if (filePath.toLowerCase().endsWith(".csv")){
            delimiter = ",";
        } else if (filePath.toLowerCase().endsWith(".tsv") || filePath.toLowerCase().endsWith(".txt")) {
            delimiter = "\t";
        } else {
            throw new IllegalArgumentException("Unsupported file format. Only CSV and TSV files work for now lol");
        }
        List<String> lines = Files.readAllLines(Path.of(filePath));
        verifyStructure(lines, delimiter);

        String header = lines.get(0);
        List<String> dataLines = lines.subList(1, lines.size());

        String fileName = Path.of(filePath).getFileName().toString();
        String schemaName = fileName.contains(".")
            ? fileName.substring(0, fileName.lastIndexOf('.'))
            : fileName;
            
        Schema schema = buildSchema(header, delimiter, schemaName);
        List<Record> records = buildRecords(dataLines, schema, delimiter);
        
        return new ParsedData(schema, records);
    }

private void verifyStructure(List<String> lines, String delimiter) {
    if (lines.isEmpty()) {
        throw new IllegalArgumentException("File is empty");
    }

    int expectedColumnCount = lines.get(0).split(delimiter, -1).length;
    for (int i = 1; i < lines.size(); i++) {
        String[] values = lines.get(i).split(delimiter, -1);
        if (values.length > expectedColumnCount) {
            throw new IllegalArgumentException(
                "Invalid structure at row " + (i + 1) + ": expected " + expectedColumnCount + " columns, but found " + values.length
            );
        }
    }
}

    private Schema buildSchema(String header, String delimiter, String schemaName) {
        String[] columnNames = header.split(delimiter, -1);
        List<Field> fields = new ArrayList<>();

        for (String columnName : columnNames) {
            fields.add(new Field(columnName, FieldType.STRING));
        }
        return new Schema(schemaName, fields);
    }

    private List<Record> buildRecords(List<String> lines, Schema schema, String delimiter) {
        List<Record> records = new ArrayList<>();
        List<Field> fields = schema.getFields();

        for (String line : lines) {
            String[] rowValues = line.split(delimiter, -1);
            Map<String, Object >values = new HashMap<>();
            Set<String> paddedFields = new HashSet<>();

            for (int i =0; i<fields.size(); i++) {
                String fieldName = fields.get(i).getName();
                if (i<rowValues.length) {
                    values.put(fieldName, rowValues[i]);
                } else {
                    values.put(fieldName, null);
                    paddedFields.add(fieldName);
                }
            }
            records.add(new Record(values, paddedFields));
        }
        return records;
    }
}