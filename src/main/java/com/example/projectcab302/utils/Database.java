package com.example.projectcab302.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:quizzes.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                try (InputStream sqlFile = Database.class.getResourceAsStream("/com/example/projectcab302/schema.sql")) {
                    if (sqlFile != null) {
                        String sql = new String(sqlFile.readAllBytes(), StandardCharsets.UTF_8);

                        try (Statement stmt = conn.createStatement()) {
                            stmt.executeUpdate(sql);
                        }

                        System.out.println("Database initialized from schema.sql");
                    } else {
                        System.err.println("schema.sql not found in resources!");
                    }
                }
                System.out.println("DB path: " + new File("quizzes.db").getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
