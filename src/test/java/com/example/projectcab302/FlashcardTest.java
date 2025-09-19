package com.example.projectcab302;

import com.example.projectcab302.Model.Flashcard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Flashcard model.
 * Each test includes a brief comment explaining what it verifies.
 */
class FlashcardTest {

    @Test
    @DisplayName("Constructor sets course/question/answer; id defaults to 0")
    void constructor_setsAllFields_andIdDefaultsToZero() {
        Flashcard card = new Flashcard("CAB302", "What is OOP?", "Object-Oriented Programming");

        assertEquals("CAB302", card.getCourse());
        assertEquals("What is OOP?", card.getQuestion());
        assertEquals("Object-Oriented Programming", card.getAnswer());
        assertEquals(0, card.getId()); // assuming int id default
    }

    @Test
    @DisplayName("setId updates id")
    void setId_updatesId() {
        Flashcard card = new Flashcard("CAB302", "Q", "A");
        card.setId(42);
        assertEquals(42, card.getId());
    }

    @Test
    @DisplayName("setCourse updates course")
    void setCourse_updatesCourse() {
        Flashcard card = new Flashcard("CAB302", "Q", "A");
        card.setCourse("EGB240");
        assertEquals("EGB240", card.getCourse());
    }

    @Test
    @DisplayName("setQuestion updates question")
    void setQuestion_updatesQuestion() {
        Flashcard card = new Flashcard("CAB302", "Old Q", "A");
        card.setQuestion("New Q");
        assertEquals("New Q", card.getQuestion());
    }

    @Test
    @DisplayName("setAnswer updates answer")
    void setAnswer_updatesAnswer() {
        Flashcard card = new Flashcard("CAB302", "Q", "Old A");
        card.setAnswer("New A");
        assertEquals("New A", card.getAnswer());
    }

    @Test
    @DisplayName("Setters allow nulls (if model intentionally permits)")
    void setters_allowNullValues() {
        Flashcard card = new Flashcard("CAB302", "Q", "A");
        card.setCourse(null);
        card.setQuestion(null);
        card.setAnswer(null);
        assertNull(card.getCourse());
        assertNull(card.getQuestion());
        assertNull(card.getAnswer());
    }

    @Test
    @DisplayName("Supports empty strings for course/question/answer")
    void supportsEmptyStrings() {
        Flashcard card = new Flashcard("", "", "");
        assertEquals("", card.getCourse());
        assertEquals("", card.getQuestion());
        assertEquals("", card.getAnswer());

        card.setCourse("");
        card.setQuestion("");
        card.setAnswer("");
        assertEquals("", card.getCourse());
        assertEquals("", card.getQuestion());
        assertEquals("", card.getAnswer());
    }
}
