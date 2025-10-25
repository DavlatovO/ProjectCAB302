package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Flashcard;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqliteFlashcardDAOTest {

    private SqliteFlashcardDAO dao;
    private Connection connection;

    @BeforeAll
    void setupInMemoryDatabase() throws Exception {
        // ✅ Create isolated SQLite memory DB for testing
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SqliteConnection.setInstanceForTests(connection);
        dao = new SqliteFlashcardDAO();
    }

    @BeforeEach
    void clearFlashcards() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM flashcards");
    }

    // ─────────────────────────────
    // addFlashcard() and getFlashcard()
    // ─────────────────────────────
    @Test
    void testAddAndRetrieveFlashcard() {
        Flashcard f = new Flashcard("CAB202", "What is AVR?", "A microcontroller architecture.");
        dao.addFlashcard(f);

        assertTrue(f.getId() > 0);
        Flashcard retrieved = dao.getFlashcard(f.getId());
        assertNotNull(retrieved);
        assertEquals("CAB202", retrieved.getCourse());
        assertEquals("What is AVR?", retrieved.getQuestion());
        assertEquals("A microcontroller architecture.", retrieved.getAnswer());
    }

    // ─────────────────────────────
    // updateFlashcard()
    // ─────────────────────────────
    @Test
    void testUpdateFlashcardChangesQuestionAndAnswer() {
        Flashcard f = new Flashcard("CAB302", "What is Java?", "A language");
        dao.addFlashcard(f);

        f.setQuestion("What is the JVM?");
        f.setAnswer("A runtime environment for Java bytecode.");
        dao.updateFlashcard(f);

        Flashcard updated = dao.getFlashcard(f.getId());
        assertEquals("What is the JVM?", updated.getQuestion());
        assertEquals("A runtime environment for Java bytecode.", updated.getAnswer());
    }

    // ─────────────────────────────
    // deleteFlashcard()
    // ─────────────────────────────
    @Test
    void testDeleteFlashcardRemovesItFromDatabase() {
        Flashcard f = new Flashcard("CAB201", "What is encapsulation?", "Hiding implementation details.");
        dao.addFlashcard(f);

        dao.deleteFlashcard(f);
        assertNull(dao.getFlashcard(f.getId()));
    }

    // ─────────────────────────────
    // getAllFlashcard()
    // ─────────────────────────────
    @Test
    void testGetAllFlashcardsReturnsAllRecords() {
        dao.addFlashcard(new Flashcard("CAB202", "Q1", "A1"));
        dao.addFlashcard(new Flashcard("CAB302", "Q2", "A2"));
        dao.addFlashcard(new Flashcard("CAB203", "Q3", "A3"));

        List<Flashcard> list = dao.getAllFlashcard();
        assertEquals(3, list.size());
        assertTrue(list.stream().anyMatch(f -> f.getQuestion().equals("Q2")));
    }

    // ─────────────────────────────
    // insertSampleData() and clearData()
    // ─────────────────────────────
    @Test
    void testInsertSampleDataThenClearData() {
        dao.insertSampleData();
        List<Flashcard> samples = dao.getAllFlashcard();
        assertFalse(samples.isEmpty());
        assertTrue(samples.size() >= 5); // you inserted 9 sample flashcards

        dao.clearData();
        assertTrue(dao.getAllFlashcard().isEmpty());
    }

    @AfterAll
    void tearDown() throws Exception {
        connection.close();
    }
}
