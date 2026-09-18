package io.github.thesaint14;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.thesaint14.inference.TypeInferer;
import io.github.thesaint14.model.Record;
import io.github.thesaint14.parser.DirectoryIngestor;
import io.github.thesaint14.parser.IngestResult;
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

Path directory = Path.of(""); //folder path here
    for (IngestResult ingestResult : new DirectoryIngestor().ingest(directory)) {
        if (!ingestResult.isSuccess()) {
            System.out.println("Failed: "+ingestResult.getFile()+ "-" + ingestResult.getError().getMessage());
            continue;
        }

        ParsedData inferred = new TypeInferer().infer(ingestResult.getData());
        System.out.println("Schema: " +inferred.getSchema());
        for (Record record : inferred.getRecords()) {
            System.out.println("Values: " + record.getValues());
            System.out.println("Padded: " + record.getPaddedFields());
        }
    }
    }

}
