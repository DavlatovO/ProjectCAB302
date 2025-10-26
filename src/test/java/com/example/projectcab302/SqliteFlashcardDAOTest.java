package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Flashcard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for SqliteFlashcardDAO.

 */
class SqliteFlashcardDAOTest {

    private SqliteFlashcardDAO dao;

    @BeforeEach
    void setUp() {
        dao = new SqliteFlashcardDAO();
        dao.clearData(); // start clean each test
    }

    @AfterEach
    void tearDown() {
        dao.clearData(); // leave DB clean
    }

    // Ensures that getAllFlashcard() returns an empty list when the table is empty.
    @Test
    void getAllFlashcard_emptyAtStart() {
        List<Flashcard> all = dao.getAllFlashcard();
        assertNotNull(all);
        assertTrue(all.isEmpty(), "Expected empty table after clear");
    }

    // Verifies addFlashcard() inserts a row and that the inserted data can be read back correctly.
    @Test
    void addFlashcard_insertsRowAndRetrievable() {
        Flashcard f = new Flashcard("CAB202", "What is AVR ldi?", "Load immediate into register");
        dao.addFlashcard(f);

        List<Flashcard> all = dao.getAllFlashcard();
        assertEquals(1, all.size(), "One row should exist after insert");

        Flashcard fromDb = all.get(0);
        assertEquals("CAB202", fromDb.getCourse());
        assertEquals("What is AVR ldi?", fromDb.getQuestion());
        assertEquals("Load immediate into register", fromDb.getAnswer());

        // Also verify getFlashcard(id) returns the same row
        Flashcard byId = dao.getFlashcard(fromDb.getId());
        assertNotNull(byId);
        assertEquals(fromDb.getId(), byId.getId());
        assertEquals(fromDb.getCourse(), byId.getCourse());
        assertEquals(fromDb.getQuestion(), byId.getQuestion());
        assertEquals(fromDb.getAnswer(), byId.getAnswer());
    }

    // Confirms getFlashcard(id) returns null for a non-existent id.
    @Test
    void getFlashcard_nonExisting_returnsNull() {
        Flashcard byId = dao.getFlashcard(999_999);
        assertNull(byId, "Non-existing id should return null");
    }

    // Checks that updateFlashcard() updates the question and answer for an existing id.
    @Test
    void updateFlashcard_updatesQuestionAndAnswer() {
        // Insert
        Flashcard f = new Flashcard("CAB302", "What is Java?", "A language.");
        dao.addFlashcard(f);

        // Get assigned id by reading back
        int id = dao.getAllFlashcard().get(0).getId();

        // Update
        Flashcard updated = new Flashcard("CAB302", "Define Java", "A statically-typed OO language and JVM platform.");
        updated.setId(id);
        dao.updateFlashcard(updated);

        Flashcard fromDb = dao.getFlashcard(id);
        assertNotNull(fromDb);
        assertEquals("CAB302", fromDb.getCourse(), "Course should remain unchanged");
        assertEquals("Define Java", fromDb.getQuestion());
        assertEquals("A statically-typed OO language and JVM platform.", fromDb.getAnswer());
    }

    // Verifies deleteFlashcard() removes exactly the targeted row and leaves others intact.
    @Test
    void deleteFlashcard_removesRow() {
        Flashcard a = new Flashcard("CAB201", "S in SOLID?", "Single Responsibility");
        Flashcard b = new Flashcard("CAB201", "O in SOLID?", "Open/Closed");
        dao.addFlashcard(a);
        dao.addFlashcard(b);

        List<Flashcard> all = dao.getAllFlashcard();
        assertEquals(2, all.size());

        int idToDelete = all.get(0).getId();
        Flashcard toDelete = new Flashcard(all.get(0).getCourse(), all.get(0).getQuestion(), all.get(0).getAnswer());
        toDelete.setId(idToDelete);

        dao.deleteFlashcard(toDelete);

        List<Flashcard> after = dao.getAllFlashcard();
        assertEquals(1, after.size(), "Exactly one row should remain");
        assertNotEquals(idToDelete, after.get(0).getId(), "Deleted id should not be present");
    }

    // Ensures insertSampleData() populates the table with the expected number of rows.
    @Test
    void insertSampleData_populatesNineRows() {
        dao.insertSampleData();
        List<Flashcard> all = dao.getAllFlashcard();
        assertEquals(9, all.size(), "Sample data should insert 9 rows");
    }

    // Confirms clearData() empties the table.
    @Test
    void clearData_emptiesTable() {
        dao.insertSampleData();
        assertFalse(dao.getAllFlashcard().isEmpty(), "Precondition: sample data inserted");

        dao.clearData();
        assertTrue(dao.getAllFlashcard().isEmpty(), "clearData should remove all rows");
    }
}
