package io.github.thesaint14.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;


public class SchemaWriter {

    private final TableGenerator tableGenerator = new TableGenerator();
    private final RecordInserter recordInserter = new RecordInserter();

    public void write(Connection conn, Schema schema, List<Record> records) throws SQLException {
        createTable(conn, schema);
        recordInserter.insertRecords(conn, schema, records);
    }

    private void createTable(Connection conn, Schema schema) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(tableGenerator.generateCreateTableSql(schema));
        }
    }
}