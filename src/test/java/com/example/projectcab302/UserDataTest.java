package com.example.projectcab302.Persistence;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDataTest {

    private Connection connection;

    @BeforeAll
    void setupInMemoryDatabase() throws Exception {
        // ✅ Use in-memory SQLite DB
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection);
        new UserData(); // auto-creates table + inserts sample data
    }

    @BeforeEach
    void clearUsersTable() throws Exception {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM users");
        }
        // Add baseline test data
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO users(username, password, email, role) VALUES " +
                    "('John', '123', 'john@123.com', 'teacher')," +
                    "('Po', '123', 'pork@123.com', 'student')");
        }
    }

    // ─────────────────────────────
    // registerUser()
    // ─────────────────────────────
    @Test
    void testRegisterUserInsertsNewUser() {
        boolean result = UserData.registerUser("Alice", "pw123", "alice@123.com", "student");
        assertTrue(result);

        // Verify it’s in the DB
        String email = UserData.getEmailForUser("Alice");
        assertEquals("alice@123.com", email);
    }

    @Test
    void testRegisterUserFailsIfUsernameTaken() {
        boolean first = UserData.registerUser("Bob", "pw", "bob@123.com", "student");
        boolean second = UserData.registerUser("Bob", "pw", "bob@123.com", "student");
        assertTrue(first);
        assertFalse(second);
    }

    @Test
    void testRegisterUserFailsOnNullInput() {
        assertFalse(UserData.registerUser(null, "pw", "a@b.com", "student"));
        assertFalse(UserData.registerUser("A", null, "a@b.com", "student"));
        assertFalse(UserData.registerUser("A", "pw", null, "student"));
        assertFalse(UserData.registerUser("A", "pw", "a@b.com", null));
    }

    // ─────────────────────────────
    // validateLoginAndGetRole()
    // ─────────────────────────────
    @Test
    void testValidateLoginReturnsCorrectRole() {
        String role = UserData.validateLoginAndGetRole("John", "123");
        assertEquals("teacher", role);
    }

    @Test
    void testValidateLoginFailsForWrongCredentials() {
        assertNull(UserData.validateLoginAndGetRole("John", "wrongpass"));
        assertNull(UserData.validateLoginAndGetRole("Unknown", "123"));
    }

    // ─────────────────────────────
    // getEmailForUser()
    // ─────────────────────────────
    @Test
    void testGetEmailForExistingUser() {
        String email = UserData.getEmailForUser("Po");
        assertEquals("pork@123.com", email);
    }

    @Test
    void testGetEmailForNonexistentUserReturnsNull() {
        assertNull(UserData.getEmailForUser("NoUser"));
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
