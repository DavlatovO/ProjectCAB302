package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SqliteCoursesDAO.

 */
class SqliteCoursesDAOTest {

    private SqliteCoursesDAO dao;

    @BeforeEach
    void setUp() {
        // Prepare a fresh DAO and clear the table so every test starts clean.
        dao = new SqliteCoursesDAO();
        dao.clearData();
    }

    @AfterEach
    void tearDown() {
        // Leave the DB clean after each test.
        dao.clearData();
    }

    // Verifies that insertSampleData() inserts exactly the expected 3 courses and they are discoverable via checkTitleExists.
    @Test
    void insertSampleData_insertsThreeCourses() {
        dao.insertSampleData();
        assertEquals(3, dao.getAllCourses().size());
        assertTrue(dao.checkTitleExists("CAB202"));
        assertTrue(dao.checkTitleExists("CAB302"));
        assertTrue(dao.checkTitleExists("CAB201"));
    }

    // Ensures addCourse persists a course and it can be read back from the database with an assigned id.
    @Test
    void addCourse_persistsAndRetrievable() {
        Course c = new Course("CAB999");
        dao.addCourse(c);

        var all = dao.getAllCourses();
        assertEquals(1, all.size());
        assertEquals("CAB999", all.get(0).getTitle());
        assertTrue(all.get(0).getId() > 0, "Inserted course should have an id when read back from DB");
    }

    // Confirms addCourse rejects duplicates (DAO throws IllegalArgumentException when title already exists).
    @Test
    void addCourse_duplicateTitle_throws() {
        dao.addCourse(new Course("CAB777"));
        assertThrows(IllegalArgumentException.class,
                () -> dao.addCourse(new Course("CAB777")),
                "Duplicate titles should be rejected by addCourse");
    }

    // Verifies getCourse(id) returns the correct course previously inserted and identified by its id.
    @Test
    void getCourse_returnsCourseById() {
        dao.addCourse(new Course("CAB555"));
        int id = dao.getAllCourses().stream()
                .filter(c -> c.getTitle().equals("CAB555"))
                .mapToInt(Course::getId)
                .findFirst()
                .orElseThrow();

        Course fromDb = dao.getCourse(id);
        assertNotNull(fromDb);
        assertEquals(id, fromDb.getId());
        assertEquals("CAB555", fromDb.getTitle());
    }

    // Ensures getCourse(id) returns null when the course id does not exist.
    @Test
    void getCourse_nonExisting_returnsNull() {
        assertNull(dao.getCourse(123456789));
    }

    // Checks that updateCourse updates the title of an existing course identified by id.
    @Test
    void updateCourse_updatesTitle() {
        // Insert original
        dao.addCourse(new Course("CAB110"));
        int id = dao.getAllCourses().stream()
                .filter(c -> c.getTitle().equals("CAB110"))
                .mapToInt(Course::getId)
                .findFirst()
                .orElseThrow();

        // Build a course object with the SAME id but a NEW title.
        Course updated = new Course("CAB111");
        updated.setId(id);

        dao.updateCourse(updated);

        Course after = dao.getCourse(id);
        assertNotNull(after);
        assertEquals("CAB111", after.getTitle());
    }

    // Verifies deleteCourse removes exactly the targeted row and leaves others intact.
    @Test
    void deleteCourse_removesRow() {
        dao.addCourse(new Course("CAB888"));
        dao.addCourse(new Course("CAB889"));

        int idToDelete = dao.getAllCourses().stream()
                .filter(c -> c.getTitle().equals("CAB888"))
                .mapToInt(Course::getId)
                .findFirst()
                .orElseThrow();

        dao.deleteCourse(new Course("CAB888") {{ setId(idToDelete); }});

        var remaining = dao.getAllCourses();
        assertEquals(1, remaining.size());
        assertEquals("CAB889", remaining.get(0).getTitle());
    }

    // Confirms checkTitleExists returns true for an existing title and false for a missing one.
    @Test
    void checkTitleExists_trueFalse() {
        dao.addCourse(new Course("CAB123"));
        assertTrue(Optional.ofNullable(dao.checkTitleExists("CAB123")).orElse(false));
        assertFalse(Optional.ofNullable(dao.checkTitleExists("NOPE")).orElse(true),
                "Non-existent title should return false");
    }
}
