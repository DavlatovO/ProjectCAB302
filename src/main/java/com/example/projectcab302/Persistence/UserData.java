package com.example.projectcab302.Persistence;

import java.sql.*;

public class UserData {
    private Connection connection;

    public UserData() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Used for testing, to be removed later
        insertSampleData();
    }

    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM users";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO users(username, password, email, role) VALUES "
                    + "('John ', '123', 'john@123.com', 'teacher'),"
                    + "('Po', '123', 'pork@123.com', 'student')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username VARCHAR NOT NULL,"
                    + "password VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL,"
                    + "role VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String username, String password, String email, String role) {
        if (username == null || password == null || email == null || role == null) return false;

        try (Connection conn = SqliteConnection.getInstance()) {
            // check if username exists
            try (PreparedStatement check = conn.prepareStatement("SELECT 1 FROM users WHERE username = ?")) {
                check.setString(1, username);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) return false; // username taken
                }
            }

            // insert new user
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)")) {
                insert.setString(1, username);
                insert.setString(2, password);
                insert.setString(3, email);
                insert.setString(4, role);
                insert.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String validateLoginAndGetRole(String username, String password) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT role FROM users WHERE username = ? AND password = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role"); // Student or Teacher
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // invalid login
    }

    public static String getEmailForUser(String username) {
        try (Connection conn = SqliteConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT email FROM users WHERE username = ?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
