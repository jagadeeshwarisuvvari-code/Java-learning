package io.github.thesaint14;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/datanexus";
        String username = "root";
        String password = System.getenv("DATANEXUS_DB_PASSWORD");
        

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected!");
        } catch (SQLException e){
            System.out.println("Connection Failed!" + e.getMessage());
         }

    }
}