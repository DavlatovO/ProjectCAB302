package com.example.projectcab302.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardTest {

    private Flashcard card;

    @BeforeEach
    void setUp() {
        card = new Flashcard("CAB302", "What is Java?", "A language");
    }

    // ---- Constructor: happy path & trimming ----

    @Test
    void constructor_trimsAllFields() {
        Flashcard c = new Flashcard("  CAB201  ", "  Q1 \n", "\t A1  ");
        assertEquals("CAB201", c.getCourse());
        assertEquals("Q1", c.getQuestion());
        assertEquals("A1", c.getAnswer());
    }

    // ---- Constructor: nulls throw ----

    @Test
    void constructor_nullCourse_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(null, "Q", "A"));
    }

    @Test
    void constructor_nullQuestion_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard("CAB302", null, "A"));
    }

    @Test
    void constructor_nullAnswer_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard("CAB302", "Q", null));
    }

    // ---- Constructor: blanks throw ----

    @Test
    void constructor_blankCourse_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard("   ", "Q", "A"));
    }

    @Test
    void constructor_blankQuestion_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard("CAB302", "   ", "A"));
    }

    @Test
    void constructor_blankAnswer_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard("CAB302", "Q", "   "));
    }

    // ---- Setters: trimming ----

    @Test
    void setCourse_trims() {
        card.setCourse("  CAB203  ");
        assertEquals("CAB203", card.getCourse());
    }

    @Test
    void setQuestion_trims() {
        card.setQuestion(" \tWhat?\n ");
        assertEquals("What?", card.getQuestion());
    }

    @Test
    void setAnswer_trims() {
        card.setAnswer("  42  ");
        assertEquals("42", card.getAnswer());
    }

    // ---- Setters: null/blank throw ----

    @Test
    void setCourse_nullOrBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setCourse(null));
        assertThrows(IllegalArgumentException.class, () -> card.setCourse("   "));
    }

    @Test
    void setQuestion_nullOrBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setQuestion(null));
        assertThrows(IllegalArgumentException.class, () -> card.setQuestion("   "));
    }

    @Test
    void setAnswer_nullOrBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setAnswer(null));
        assertThrows(IllegalArgumentException.class, () -> card.setAnswer("   "));
    }

    // ---- id behavior ----

    @Test
    void id_defaultsToZero_andCanBeSet() {
        Flashcard c = new Flashcard("CAB230", "Q", "A");
        assertEquals(0, c.getId());
        c.setId(123);
        assertEquals(123, c.getId());
    }
}
