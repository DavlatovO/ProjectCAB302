package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.User;

/**
 * Interface for the User Data Access Object (DAO).
 * <p>
 * This interface defines the contract for all operations
 * related to user data in the database. Implementations of this
 * interface provide mechanisms to create, authenticate, and
 * manage user records in persistent storage.
 * </p>
 */
public interface IUserDAO {

    /**
     * Creates a new user in the database.
     *
     * @param user The {@link User} object to be stored.
     */
    void createUser(User user);

    /**
     * Deletes an existing user from the database.
     *
     * @param user The {@link User} object to delete.
     */
    void deleteUser(User user);

    /**
     * Checks if a given email address is already registered in the database.
     *
     * @param user_id The id to get the user
     * @return {@code true} if the email exists, {@code false} otherwise.
     */
    User getUser(int user_id);

    /**
     * Checks if a given email address is already registered in the database.
     *
     * @param email The email address to check.
     * @return {@code true} if the email exists, {@code false} otherwise.
     */
    boolean emailExists(String email);

    /**
     * Authenticates a user by verifying their credentials.
     *
     * @param username      The username provided by the user.
     * @param plainPassword The plain text password entered by the user.
     * @return A fully populated {@link User} object if authentication is successful,
     *         or {@code null} if authentication fails.
     */
    User login(String username, String plainPassword);
}
