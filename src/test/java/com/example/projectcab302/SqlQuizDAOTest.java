package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Quiz;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqlQuizDAOTest {

    private SqlQuizDAO dao;
    private Connection connection;

    @BeforeAll
    void setupInMemoryDatabase() throws Exception {
        // ✅ Use in-memory SQLite for isolation
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection);
        dao = new SqlQuizDAO();
    }

    @BeforeEach
    void clearTable() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM quiz");
    }

    // ─────────────────────────────
    // addQuiz() + getQuiz()
    // ─────────────────────────────
    @Test
    void testAddAndRetrieveQuiz() {
        Quiz q = new Quiz("CAB302", "What is Java?", "Lang", "Tool", "OS", "App", "Lang", 5, 2);
        dao.addQuiz(q);

        assertTrue(q.getQuizID() > 0);
        Quiz fetched = dao.getQuiz(q.getQuizID());
        assertNotNull(fetched);
        assertEquals("CAB302", fetched.getCourse());
        assertEquals("What is Java?", fetched.getQuizQuestion());
        assertEquals("Lang", fetched.getCorrectAnswer());
        assertEquals(5, fetched.getCorrect());
    }

    // ─────────────────────────────
    // updateQuiz()
    // ─────────────────────────────
    @Test
    void testUpdateQuizChangesFields() {
        Quiz q = new Quiz("CAB202", "Old Question", "A", "B", "C", "D", "A", 1, 1);
        dao.addQuiz(q);
        q.setQuizQuestion("New Question");
        q.setCorrectAnswer("B");
        dao.updateQuiz(q);

        Quiz updated = dao.getQuiz(q.getQuizID());
        assertEquals("New Question", updated.getQuizQuestion());
        assertEquals("B", updated.getCorrectAnswer());
    }

    // ─────────────────────────────
    // deleteQuiz()
    // ─────────────────────────────
    @Test
    void testDeleteQuizRemovesRecord() {
        Quiz q = new Quiz("CAB201", "Delete me", "1", "2", "3", "4", "1", 0, 0);
        dao.addQuiz(q);
        dao.deleteQuiz(q);
        assertNull(dao.getQuiz(q.getQuizID()));
    }

    // ─────────────────────────────
    // getAllQuizs()
    // ─────────────────────────────
    @Test
    void testGetAllQuizsReturnsAll() {
        dao.addQuiz(new Quiz("CAB202", "Q1", "A1", "A2", "A3", "A4", "A1", 1, 0));
        dao.addQuiz(new Quiz("CAB202", "Q2", "B1", "B2", "B3", "B4", "B2", 2, 0));
        dao.addQuiz(new Quiz("CAB302", "Q3", "C1", "C2", "C3", "C4", "C3", 3, 1));

        List<Quiz> all = dao.getAllQuizs();
        assertEquals(3, all.size());
        assertTrue(all.stream().anyMatch(q -> q.getQuizQuestion().equals("Q3")));
    }

    // ─────────────────────────────
    // getAllQuestionsfromCourse()
    // ─────────────────────────────
    @Test
    void testGetAllQuestionsFromCourseFiltersCorrectly() {
        dao.addQuiz(new Quiz("CAB202", "Q1", "A", "B", "C", "D", "A", 1, 0));
        dao.addQuiz(new Quiz("CAB302", "Q2", "A", "B", "C", "D", "B", 2, 1));

        List<Quiz> cab202 = dao.getAllQuestionsfromCourse("CAB202");
        assertEquals(1, cab202.size());
        assertEquals("CAB202", cab202.get(0).getCourse());
    }

    // ─────────────────────────────
    // updateQuizMetrics()
    // ─────────────────────────────
    @Test
    void testUpdateQuizMetricsIncrementsCorrectOrWrong() {
        Quiz q = new Quiz("CAB302", "Metric test", "A", "B", "C", "D", "A", 2, 3);
        dao.addQuiz(q);

        dao.updateQuizMetrics(q, true);
        Quiz afterCorrect = dao.getQuiz(q.getQuizID());
        assertEquals(3, afterCorrect.getCorrect());
        assertEquals(3, afterCorrect.getWrong());

        dao.updateQuizMetrics(afterCorrect, false);
        Quiz afterWrong = dao.getQuiz(q.getQuizID());
        assertEquals(3, afterWrong.getCorrect());
        assertEquals(4, afterWrong.getWrong());
    }

    // ─────────────────────────────
    // insertSampleData() + clearData()
    // ─────────────────────────────
    @Test
    void testInsertSampleDataAndClearData() {
        dao.insertSampleData();
        List<Quiz> samples = dao.getAllQuizs();
        assertFalse(samples.isEmpty());
        assertTrue(samples.size() >= 4);

        dao.clearData();
        assertTrue(dao.getAllQuizs().isEmpty());
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
