package io.github.thesaint14.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import io.github.thesaint14.model.Field;
import io.github.thesaint14.model.Record;
import io.github.thesaint14.model.Schema;


public class RecordInserter {
    private static final int BATCH_SIZE = 500;

    public void insertRecords(Connection conn, Schema schema, List<Record> records) throws SQLException {
        String sql = buildInsertSql(schema);

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(sql)){
            int count = 0;
            for (Record record : records) {
                bindValues(ps, schema, record);
                ps.addBatch();
                if (++count % BATCH_SIZE == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
        }catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private  String buildInsertSql(Schema schema) {
        String columns = schema.getFields().stream()
                .map(f -> "`" + SqlIdentifiers.validate(f.getName()) +"`")
                .collect(Collectors.joining(", "));
        String placeholders = schema.getFields().stream()
                .map(f -> "?")
                .collect(Collectors.joining(", "));
        return "INSERT INTO `" + SqlIdentifiers.validate(schema.getName()) + "` (" + columns + ") VALUES (" + placeholders + ")";            
    }

    private void bindValues(PreparedStatement ps, Schema schema, Record record) throws SQLException {
        List<Field> fields = schema.getFields();
        for (int i =0; i<fields.size(); i++){
            ps.setObject(i+1, record.getValue(fields.get(i).getName()));
        }
    }
    
}
