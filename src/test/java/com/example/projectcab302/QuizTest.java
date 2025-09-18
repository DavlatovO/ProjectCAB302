package com.example.projectcab302;

import com.example.projectcab302.Model.Quiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Quiz model.
 * Covers constructors, getters/setters, toString, and edge cases.
 */
class QuizTest {

    @Test
    @DisplayName("Constructor with quizID should set all fields correctly")
    void constructor_withId_setsAllFields() {
        Quiz quiz = new Quiz(1, "What is Java?", "Language", "Platform", "Coffee", "OS", "Language");

        assertEquals(1, quiz.getQuizID());
        assertEquals("What is Java?", quiz.getQuizQuestion());
        assertEquals("Language", quiz.getAnswer1());
        assertEquals("Platform", quiz.getAnswer2());
        assertEquals("Coffee", quiz.getAnswer3());
        assertEquals("OS", quiz.getAnswer4());
        assertEquals("Language", quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Constructor without quizID should default quizID to 0")
    void constructor_withoutId_defaultsQuizIDToZero() {
        Quiz quiz = new Quiz(
                "What is Java?",
                "Language",
                "Platform",
                "Coffee",
                "OS",
                "Language"
        );

        assertEquals(0, quiz.getQuizID()); // Java default int = 0
        assertEquals("What is Java?", quiz.getQuizQuestion());
    }

    @Test
    @DisplayName("setQuizID should update quizID field")
    void setQuizID_updatesValue() {
        Quiz quiz = new Quiz("Q", "A1", "A2", "A3", "A4", "A1");

        quiz.setQuizID(42);

        assertEquals(42, quiz.getQuizID());
    }

    @Test
    @DisplayName("Setters should update corresponding fields")
    void setters_updateFields() {
        Quiz quiz = new Quiz("Q", "A1", "A2", "A3", "A4", "A1");

        quiz.setQuizQuestion("New Question");
        quiz.setAnswer1("New A1");
        quiz.setAnswer2("New A2");
        quiz.setAnswer3("New A3");
        quiz.setAnswer4("New A4");
        quiz.setCorrectAnswer("New Correct");

        assertEquals("New Question", quiz.getQuizQuestion());
        assertEquals("New A1", quiz.getAnswer1());
        assertEquals("New A2", quiz.getAnswer2());
        assertEquals("New A3", quiz.getAnswer3());
        assertEquals("New A4", quiz.getAnswer4());
        assertEquals("New Correct", quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Setters should throw exception on null (matching NOT NULL DB constraint)")
    void setters_disallowNulls() {
        Quiz quiz = new Quiz("Q", "A1", "A2", "A3", "A4", "A1");

        assertThrows(IllegalArgumentException.class, () -> quiz.setQuizQuestion(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setAnswer1(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setAnswer2(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setAnswer3(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setAnswer4(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setCorrectAnswer(null));
        assertThrows(IllegalArgumentException.class, () -> quiz.setQuizID(null));
    }

    @Test
    @DisplayName("toString should contain key field values")
    void toString_includesFieldValues() {
        Quiz quiz = new Quiz(7, "Q?", "A1", "A2", "A3", "A4", "A2");

        String str = quiz.toString();

        assertTrue(str.contains("quizID=7"));
        assertTrue(str.contains("Q?"));
        assertTrue(str.contains("A1"));
        assertTrue(str.contains("A2"));
        assertTrue(str.contains("A3"));
        assertTrue(str.contains("A4"));
        assertTrue(str.contains("correctAnswer=A2"));
    }

    // --- Edge Cases ---

    @Test
    @DisplayName("Supports empty strings for question and answers")
    void supportsEmptyStrings() {
        Quiz quiz = new Quiz("", "", "", "", "", "");
        assertEquals("", quiz.getQuizQuestion());
        assertEquals("", quiz.getAnswer1());
        assertEquals("", quiz.getAnswer2());
        assertEquals("", quiz.getAnswer3());
        assertEquals("", quiz.getAnswer4());
        assertEquals("", quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Handles very long strings without truncation")
    void handlesVeryLongStrings() {
        String longString = "x".repeat(10_000);
        Quiz quiz = new Quiz(longString, longString, longString, longString, longString, longString);

        assertEquals(longString, quiz.getQuizQuestion());
        assertEquals(longString, quiz.getAnswer1());
        assertEquals(longString, quiz.getAnswer2());
        assertEquals(longString, quiz.getAnswer3());
        assertEquals(longString, quiz.getAnswer4());
        assertEquals(longString, quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Allows duplicate answers across multiple fields")
    void allowsDuplicateAnswers() {
        Quiz quiz = new Quiz("Q", "Same", "Same", "Same", "Same", "Same");
        assertEquals("Same", quiz.getAnswer1());
        assertEquals("Same", quiz.getAnswer2());
        assertEquals("Same", quiz.getAnswer3());
        assertEquals("Same", quiz.getAnswer4());
        assertEquals("Same", quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Correct answer not required to match provided answers")
    void correctAnswer_canDifferFromOptions() {
        Quiz quiz = new Quiz("Q", "A1", "A2", "A3", "A4", "Different");
        assertEquals("Different", quiz.getCorrectAnswer());
    }

    @Test
    @DisplayName("Supports negative quizID values")
    void supportsNegativeQuizID() {
        Quiz quiz = new Quiz(-99, "Q", "A1", "A2", "A3", "A4", "A1");
        assertEquals(-99, quiz.getQuizID());
    }
}
