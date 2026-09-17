// io/github/thesaint14/db/TableGenerator.java
package io.github.thesaint14.db;

import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.FieldType;
import io.github.thesaint14.model.Schema;

import java.util.stream.Collectors;

public class TableGenerator {

    public String generateCreateTableSql(Schema schema) {
        String columns = schema.getFields().stream()
                .map(this::columnDefinition)
                .collect(Collectors.joining(", "));

        return "CREATE TABLE IF NOT EXISTS `" + SqlIdentifiers.validate(schema.getName())
                + "` (" + columns + ")";
    }

    private String columnDefinition(Field field) {
        return "`" + SqlIdentifiers.validate(field.getName()) + "` " + mapSqlType(field.getType());
    }

    private String mapSqlType(FieldType type) {
        return switch (type) {
            case INTEGER -> "INT";
            case LONG -> "BIGINT";
            case DOUBLE -> "DOUBLE";
            case BOOLEAN -> "BOOLEAN";
            case DATE -> "DATE";
            case STRING -> "VARCHAR(255)";
        };
    }
}