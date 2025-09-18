package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.Utils.Hashing;

import java.sql.*;

public class DatabaseController {
    private static final String DB_URL = "jdbc:sqlite:database.db";

    public static void initDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL,
                    role TEXT NOT NULL
                );
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        String sql = "INSERT INTO users(username, email, role, password) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getHashedPassword());


            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User login(String username, String plainPassword) {
        String sql = "SELECT role, email, password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println(role);
                String email = rs.getString("email");
                System.out.println(email);
                String storedHash = rs.getString("password");
                System.out.println(storedHash);
                String inputHash = Hashing.hashPassword(plainPassword);


                if (storedHash.equals(inputHash)) {
                    User.Roles roleEnum = User.Roles.valueOf(role); // throws if invalid
                    return switch (roleEnum) {
                        case Student -> new Student(username, email, roleEnum, storedHash);
                        case Teacher -> new Teacher(username, email, roleEnum, storedHash);
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
