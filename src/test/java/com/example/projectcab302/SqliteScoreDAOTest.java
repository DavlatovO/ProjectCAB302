package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Score;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteScoreDAOTest {

    private SqliteScoreDAO dao;
    private Connection connection;

    @BeforeAll
    void setupInMemoryDatabase() throws Exception {
        // ✅ Use an in-memory SQLite database so no files are touched
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection);
        dao = new SqliteScoreDAO();
    }

    @BeforeEach
    void clearTable() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM scores");
    }

    // ─────────────────────────────
    // addScore() and getScore()
    // ─────────────────────────────
    @Test
    void testAddScoreAndRetrieve() {
        Score s = new Score(101, 0.75, 0.82, 5, 3);
        dao.addScore(s);

        Score fetched = dao.getScore(101);
        assertNotNull(fetched);
        assertEquals(0.75, fetched.getQuizScore(), 0.001);
        assertEquals(0.82, fetched.getPvpScore(), 0.001);
        assertEquals(5, fetched.getPvpBattle());
        assertEquals(3, fetched.getQuizAttempts());
    }

    // ─────────────────────────────
    // updateScore()
    // ─────────────────────────────
    @Test
    void testUpdateScoreChangesValues() {
        Score s = new Score(102, 0.6, 0.7, 2, 1);
        dao.addScore(s);

        s.setQuizScore(0.9);
        s.setPvpScore(0.8);
        s.setPvpBattle(4);
        s.setQuizAttempts(3);
        dao.updateScore(s);

        Score updated = dao.getScore(102);
        assertEquals(0.9, updated.getQuizScore(), 0.001);
        assertEquals(0.8, updated.getPvpScore(), 0.001);
        assertEquals(4, updated.getPvpBattle());
        assertEquals(3, updated.getQuizAttempts());
    }

    // ─────────────────────────────
    // deleteScore()
    // ─────────────────────────────
    @Test
    void testDeleteScoreRemovesIt() {
        Score s = new Score(103, 0.5, 0.5, 1, 1);
        dao.addScore(s);

        dao.deleteScore(s);
        assertNull(dao.getScore(103));
    }

    // ─────────────────────────────
    // getAllScores()
    // ─────────────────────────────
    @Test
    void testGetAllScoresReturnsAll() {
        dao.addScore(new Score(201, 0.6, 0.6, 1, 1));
        dao.addScore(new Score(202, 0.7, 0.7, 2, 2));
        dao.addScore(new Score(203, 0.8, 0.8, 3, 3));

        List<Score> all = dao.getAllScores();
        assertEquals(3, all.size());
        assertTrue(all.stream().anyMatch(s -> s.getStudentID() == 202));
    }

    // ─────────────────────────────
    // clearData() and insertSampleData()
    // ─────────────────────────────
    @Test
    void testInsertSampleDataThenClearData() {
        dao.insertSampleData();
        List<Score> samples = dao.getAllScores();
        assertFalse(samples.isEmpty());
        assertEquals(3, samples.size());

        dao.clearData();
        assertTrue(dao.getAllScores().isEmpty());
    }

    // ─────────────────────────────
    // updatePVPScore()
    // ─────────────────────────────
    @Test
    void testUpdatePVPScoreAveragesAndIncrementsBattles() {
        Score s = new Score(301, 0.5, 0.5, 2, 0);
        dao.addScore(s);

        dao.updatePVPScore(301, 1); // simulate win
        Score updated = dao.getScore(301);

        double expected = (0.5 + 1) / 2.0; // averaging old + new
        assertEquals(expected, updated.getPvpScore(), 0.001);
        assertEquals(3, updated.getPvpBattle());
    }

    // ─────────────────────────────
    // updateQuizScore()
    // ─────────────────────────────
    @Test
    void testUpdateQuizScoreAveragesAndIncrementsAttempts() {
        Score s = new Score(302, 0.8, 0.9, 0, 1);
        dao.addScore(s);

        dao.updateQuizScore(302, 0.6);
        Score updated = dao.getScore(302);

        double expected = (0.8 + 0.6) / 2.0;
        assertEquals(expected, updated.getQuizScore(), 0.001);
        assertEquals(2, updated.getQuizAttempts());
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
