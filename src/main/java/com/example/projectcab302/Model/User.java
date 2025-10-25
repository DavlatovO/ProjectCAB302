package com.example.projectcab302.Model;

import com.example.projectcab302.Utils.Hashing;

public abstract class User {
    private int id;
    private String username;
    private String email;
    private Roles role;
    private String password;

    public enum Roles {
        Student,
        Teacher
    }

    public User(String username, String email, Roles role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // ─────────────────────────────
    // Getters
    // ─────────────────────────────
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Roles getRoles() {
        return role;
    }

    public String getHashedPassword() {
        return Hashing.hashPassword(password);
    }

    public abstract String getRole();

    // ─────────────────────────────
    // Setters
    // ─────────────────────────────
    public void setId(int id) {
        if (id < 0) throw new IllegalArgumentException("ID cannot be negative");
        this.id = id;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        this.username = username;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setRole(Roles role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.password = password;
    }
}
