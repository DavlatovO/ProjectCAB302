package com.example.projectcab302.Model;

import com.example.projectcab302.Utils.Hashing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private Student student;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        student = new Student("alice", "alice@example.com", User.Roles.Student, "password123");
        teacher = new Teacher("bob", "bob@example.com", User.Roles.Teacher, "secret456");
    }

    // ─────────────────────────────
    // Constructor & Getters
    // ─────────────────────────────
    @Test
    void testConstructorAndBasicGetters() {
        assertEquals("alice", student.getUsername());
        assertEquals("alice@example.com", student.getEmail());
        assertEquals(User.Roles.Student, student.getRoles());

        assertEquals("bob", teacher.getUsername());
        assertEquals("bob@example.com", teacher.getEmail());
        assertEquals(User.Roles.Teacher, teacher.getRoles());
    }

    // ─────────────────────────────
    // Role Overrides
    // ─────────────────────────────
    @Test
    void testRoleOverridesReturnCorrectStrings() {
        assertEquals("Student", student.getRole());
        assertEquals("Teacher", teacher.getRole());
    }

    // ─────────────────────────────
    // ID Handling
    // ─────────────────────────────
    @Test
    void testSetAndGetId() {
        student.setId(10);
        teacher.setId(20);
        assertEquals(10, student.getId());
        assertEquals(20, teacher.getId());
    }

    // ─────────────────────────────
    // Password Hashing
    // ─────────────────────────────
    @Test
    void testPasswordHashingIsConsistentAndNotPlaintext() {
        String studentHash = student.getHashedPassword();
        String teacherHash = teacher.getHashedPassword();

        // Hashes should not match raw passwords
        assertNotEquals("password123", studentHash);
        assertNotEquals("secret456", teacherHash);

        // Deterministic behavior
        assertEquals(studentHash, Hashing.hashPassword("password123"));
        assertEquals(teacherHash, Hashing.hashPassword("secret456"));

        // Hashes should differ between users
        assertNotEquals(studentHash, teacherHash);
    }

    // ─────────────────────────────
    // Instance Independence
    // ─────────────────────────────
    @Test
    void testDifferentInstancesRemainIndependent() {
        assertNotEquals(student.getUsername(), teacher.getUsername());
        assertNotEquals(student.getEmail(), teacher.getEmail());
        assertNotEquals(student.getHashedPassword(), teacher.getHashedPassword());
    }
}
