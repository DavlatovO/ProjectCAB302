package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.*;
import com.example.projectcab302.Utils.Hashing;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteUserDAOTest {

    private SqliteUserDAO userDAO;
    private Connection connection;

    @BeforeAll
    void setupDatabase() throws Exception {
        // ✅ Use an in-memory SQLite database so tests don’t persist data
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection); // custom helper for testing
        userDAO = new SqliteUserDAO();
    }

    @BeforeEach
    void clearBeforeEach() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM users");
    }

    @Test
    void testCreateUserInsertsRecordAndAssignsId() {
        Student student = new Student("alice", "alice@example.com", User.Roles.Student, "password123");
        userDAO.createUser(student);

        assertTrue(student.getId() > 0); // id should be auto-generated
        assertTrue(userDAO.emailExists("alice@example.com"));
    }

    @Test
    void testGetAllStudentsReturnsAllCreatedStudents() {
        Student s1 = new Student("john", "john@example.com", User.Roles.Student, "123");
        Student s2 = new Student("maria", "maria@example.com", User.Roles.Student, "456");
        userDAO.createUser(s1);
        userDAO.createUser(s2);

        List<Student> students = userDAO.getAllStudents();

        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getUsername().equals("john")));
        assertTrue(students.stream().anyMatch(s -> s.getUsername().equals("maria")));
    }

    @Test
    void testDeleteUserRemovesRecord() {
        Student s = new Student("temp", "temp@example.com", User.Roles.Student, "temp123");
        userDAO.createUser(s);
        int id = s.getId();

        userDAO.deleteUser(s);
        assertNull(userDAO.getStudent(id));
    }

    @Test
    void testUpdateUserChangesFields() {
        Student s = new Student("oliver", "oliver@example.com", User.Roles.Student, "init123");
        userDAO.createUser(s);

        s.setUsername("oliver2");
        s.setEmail("oliver2@example.com");
        userDAO.updateUser(s);

        Student updated = userDAO.getStudent(s.getId());
        assertEquals("oliver2", updated.getUsername());
        assertEquals("oliver2@example.com", updated.getEmail());
    }

    @Test
    void testLoginReturnsCorrectUserWithValidCredentials() {
        String rawPassword = "mypassword";
        String hashed = Hashing.hashPassword(rawPassword);

        // Manually insert a test record
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                INSERT INTO users (id, username, email, role, password)
                VALUES (1, 'bob', 'bob@example.com', 'Student', '%s')
                """.formatted(hashed));
        } catch (Exception e) {
            fail(e);
        }

        User loggedIn = userDAO.login("bob", rawPassword);

        assertNotNull(loggedIn);
        assertEquals("bob", loggedIn.getUsername());
        assertEquals("Student", loggedIn.getRole());
        assertEquals(1, loggedIn.getId());
    }

    @Test
    void testLoginFailsWithWrongPassword() {
        Student s = new Student("kate", "kate@example.com", User.Roles.Student, "goodpass");
        userDAO.createUser(s);

        User invalid = userDAO.login("kate", "wrongpass");
        assertNull(invalid);
    }

    @Test
    void testEmailExistsReturnsTrueIfPresent() {
        Student s = new Student("ben", "ben@example.com", User.Roles.Student, "pw");
        userDAO.createUser(s);

        assertTrue(userDAO.emailExists("ben@example.com"));
        assertFalse(userDAO.emailExists("unknown@example.com"));
    }

    @Test
    void testGetStudentByIdReturnsCorrectUser() {
        Student s = new Student("lily", "lily@example.com", User.Roles.Student, "pw");
        userDAO.createUser(s);

        Student fetched = userDAO.getStudent(s.getId());
        assertNotNull(fetched);
        assertEquals("lily", fetched.getUsername());
        assertEquals(s.getId(), fetched.getId());
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
