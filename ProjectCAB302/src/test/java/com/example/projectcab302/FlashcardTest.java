package org.example.flashcard.Model;

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
    @DisplayName("Constructor should set question and answer; id defaults to 0")
    void constructor_setsFields_andIdDefaultsToZero() {
        // Arrange & Act
        Flashcard card = new Flashcard("What is OOP?", "Object-Oriented Programming", answer);

        // Assert
        // Verifies constructor correctly assigns question/answer
        assertEquals("What is OOP?", card.getQuestion());
        assertEquals("Object-Oriented Programming", card.getAnswer());

        // Verifies default int field 'id' is 0 (Java default for int)
        assertEquals(0, card.getId());
    }

    @Test
    @DisplayName("setId should update the id field")
    void setId_updatesId() {
        Flashcard card = new Flashcard("Q", "A", answer);

        // Act
        card.setId(42);

        // Assert
        // Verifies setter stores the provided integer value
        assertEquals(42, card.getId());
    }

    @Test
    @DisplayName("setQuestion should update the question field")
    void setQuestion_updatesQuestion() {
        Flashcard card = new Flashcard("Old question", "A", answer);

        // Act
        card.setQuestion("New question");

        // Assert
        // Verifies question mutability through setter
        assertEquals("New question", card.getQuestion());
    }

    @Test
    @DisplayName("setAnswer should update the answer field")
    void setAnswer_updatesAnswer() {
        Flashcard card = new Flashcard("Q", "Old answer", answer);

        // Act
        card.setAnswer("New answer");

        // Assert
        // Verifies answer mutability through setter
        assertEquals("New answer", card.getAnswer());
    }

    @Test
    @DisplayName("Setters should allow nulls (if desired behavior) for question/answer")
    void setters_allowNullValues() {
        Flashcard card = new Flashcard("Q", "A", answer);

        // Act
        card.setQuestion(null);
        card.setAnswer(null);

        // Assert
        // Verifies that the class accepts null (no validation in the model)
        assertNull(card.getQuestion());
        assertNull(card.getAnswer());
    }

    @Test
    @DisplayName("Supports empty strings for question/answer")
    void supportsEmptyStrings() {
        Flashcard card = new Flashcard("", "", answer);

        // Assert
        // Verifies empty strings are stored as-is
        assertEquals("", card.getQuestion());
        assertEquals("", card.getAnswer());

        // Act
        card.setQuestion("");
        card.setAnswer("");

        // Assert again after setters
        assertEquals("", card.getQuestion());
        assertEquals("", card.getAnswer());
    }
}
