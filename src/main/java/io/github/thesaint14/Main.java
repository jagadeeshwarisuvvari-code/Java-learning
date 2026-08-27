package io.github.thesaint14;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.thesaint14.model.Record;
import io.github.thesaint14.parser.CsvParser;
import io.github.thesaint14.parser.ParsedData;

public class Main {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/datanexus";
        String username = "root";
        String password = System.getenv("DATANEXUS_DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected!");
        } catch (SQLException e) {
            System.out.println("Connection Failed!" + e.getMessage());
        }

        // --- Parser test ---
        CsvParser parser = new CsvParser();
        ParsedData result = parser.parse(""); //insert yo file path here, it work yayyyy!

        System.out.println("Schema: " + result.getSchema());
        for (Record record : result.getRecords()) {
            System.out.println("Values: " + record.getValues());
            System.out.println("Padded: " + record.getPaddedFields());
        }
    }
}
