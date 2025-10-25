package com.example.projectcab302.Model;

import com.example.projectcab302.Utils.Hashing;

/**
 * Represents a general user in the system.
 * <p>
 * This abstract class defines common attributes and behavior shared
 * by all users, such as username, email, role, and password.
 * Concrete subclasses such as {@link Student} and {@link Teacher}
 * provide specific implementations of {@link #getRole()}.
 * </p>
 */
public abstract class User {

    /** The unique identifier for the user. */
    private int id;

    /** The username chosen by the user. */
    private String username;

    /** The email address associated with the user. */
    private String email;

    /** The user’s assigned role (e.g., Student or Teacher). */
    private Roles role;

    /** The plain-text password (hashed via {@link #getHashedPassword()}). */
    private String password;

    /**
     * Enumeration of possible user roles within the system.
     */
    public enum Roles {
        /** Represents a student account. */
        Student,
        /** Represents a teacher account. */
        Teacher
    }

    // ─────────────────────────────
    // Constructor
    // ─────────────────────────────

    /**
     * Constructs a new {@code User} with the specified details.
     *
     * @param username the username of the user
     * @param email    the user's email address
     * @param role     the role of the user (Student or Teacher)
     * @param password the user's plain-text password
     */
    public User(String username, String email, Roles role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // ─────────────────────────────
    // Getters
    // ─────────────────────────────

    /**
     * Returns the user's unique ID.
     *
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's email address.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the user's role as an enum constant.
     *
     * @return the user's {@link Roles} value
     */
    public Roles getRoles() {
        return role;
    }

    /**
     * Returns the user's password hashed using SHA-256 or the
     * algorithm defined in {@link Hashing#hashPassword(String)}.
     *
     * @return the hashed password
     */
    public String getHashedPassword() {
        return Hashing.hashPassword(password);
    }

    /**
     * Returns the user's role name as a {@link String}.
     * <p>
     * This method must be implemented by subclasses (e.g., returning
     * "Student" or "Teacher").
     * </p>
     *
     * @return the role name of the user
     */
    public abstract String getRole();

    // ─────────────────────────────
    // Setters
    // ─────────────────────────────

    /**
     * Sets the user's unique ID.
     *
     * @param id the ID to assign
     * @throws IllegalArgumentException if the ID is negative
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = id;
    }

    /**
     * Sets the username for the user.
     *
     * @param username the username to set
     * @throws IllegalArgumentException if the username is {@code null} or blank
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        this.username = username;
    }

    /**
     * Sets the email address for the user.
     *
     * @param email the email to set
     * @throws IllegalArgumentException if the email is {@code null}, blank,
     *                                  or not in a valid format
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    /**
     * Sets the user's role.
     *
     * @param role the role to assign
     * @throws IllegalArgumentException if the role is {@code null}
     */
    public void setRole(Roles role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        this.role = role;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     * @throws IllegalArgumentException if the password is {@code null} or blank
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        this.password = password;
    }
}
