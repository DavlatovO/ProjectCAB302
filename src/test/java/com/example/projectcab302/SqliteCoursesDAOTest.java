package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteCoursesDAOTest {

    private SqliteCoursesDAO dao;
    private Connection connection;

    @BeforeAll
    void setupInMemoryDatabase() throws Exception {
        // ✅ Use SQLite in-memory DB for isolation
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection);   // helper added in SqliteConnection
        dao = new SqliteCoursesDAO();
    }

    @BeforeEach
    void clearTable() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM courses");
    }

    // ─────────────────────────────
    // addCourse / getCourse
    // ─────────────────────────────
    @Test
    void testAddCourseAndRetrieveById() {
        Course course = new Course("CAB302");
        dao.addCourse(course);

        assertTrue(course.getId() > 0);
        Course fetched = dao.getCourse(course.getId());
        assertNotNull(fetched);
        assertEquals("CAB302", fetched.getTitle());
    }

    // ─────────────────────────────
    // Duplicate protection
    // ─────────────────────────────
    @Test
    void testAddDuplicateTitleThrowsException() {
        dao.addCourse(new Course("CAB202"));
        assertThrows(IllegalArgumentException.class,
                () -> dao.addCourse(new Course("CAB202")));
    }

    // ─────────────────────────────
    // Update course
    // ─────────────────────────────
    @Test
    void testUpdateCourseChangesTitle() {
        Course c = new Course("CAB201");
        dao.addCourse(c);
        c.setTitle("CAB203");
        dao.updateCourse(c);

        Course updated = dao.getCourse(c.getId());
        assertEquals("CAB203", updated.getTitle());
    }

    // ─────────────────────────────
    // Delete course
    // ─────────────────────────────
    @Test
    void testDeleteCourseRemovesRecord() {
        Course c = new Course("CAB301");
        dao.addCourse(c);

        dao.deleteCourse(c);
        assertNull(dao.getCourse(c.getId()));
    }

    // ─────────────────────────────
    // getAllCourses
    // ─────────────────────────────
    @Test
    void testGetAllCoursesReturnsAllRecords() {
        dao.addCourse(new Course("CAB202"));
        dao.addCourse(new Course("CAB302"));
        dao.addCourse(new Course("CAB203"));

        List<Course> courses = dao.getAllCourses();
        assertEquals(3, courses.size());
        assertTrue(courses.stream().anyMatch(c -> c.getTitle().equals("CAB202")));
    }

    // ─────────────────────────────
    // checkTitleExists
    // ─────────────────────────────
    @Test
    void testCheckTitleExistsWorksCorrectly() {
        dao.addCourse(new Course("CAB404"));
        assertTrue(dao.checkTitleExists("CAB404"));
        assertFalse(dao.checkTitleExists("CAB999"));
    }

    // ─────────────────────────────
    // insertSampleData / clearData
    // ─────────────────────────────
    @Test
    void testInsertSampleDataAndClearData() {
        dao.insertSampleData();
        List<Course> all = dao.getAllCourses();
        assertEquals(3, all.size());  // CAB202, CAB302, CAB201

        dao.clearData();
        assertEquals(0, dao.getAllCourses().size());
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
