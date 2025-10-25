package com.example.projectcab302.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuizTest {

    private Quiz quizFull;
    private Quiz quizBasic;

    @BeforeEach
    void setUp() {
        quizFull = new Quiz(
                "Math",
                "What is 2 + 2?",
                "3", "4", "5", "6",
                "4",
                8, 2
        );

        quizBasic = new Quiz(
                "Science",
                "What is H2O?",
                "Oxygen", "Hydrogen", "Water", "Steam",
                "Water"
        );
    }

    // ---- Constructor Tests ----
    @Test
    void testConstructorWithStats() {
        assertEquals("Math", quizFull.getCourse());
        assertEquals("What is 2 + 2?", quizFull.getQuizQuestion());
        assertEquals("4", quizFull.getCorrectAnswer());
        assertEquals(8, quizFull.getCorrect());
        assertEquals(2, quizFull.getWrong());
    }

    @Test
    void testConstructorWithoutStats() {
        assertEquals("Science", quizBasic.getCourse());
        assertEquals("What is H2O?", quizBasic.getQuizQuestion());
        assertEquals("Water", quizBasic.getCorrectAnswer());
        assertEquals(0, quizBasic.getCorrect());
        assertEquals(0, quizBasic.getWrong());
    }

    // ---- Setter Tests ----
    @Test
    void testSettersAndGetters() {
        quizFull.setQuizID(10);
        quizFull.setCourse("Programming");
        quizFull.setQuizQuestion("What is Java?");
        quizFull.setAnswer1("Language");
        quizFull.setAnswer2("IDE");
        quizFull.setAnswer3("Framework");
        quizFull.setAnswer4("OS");
        quizFull.setCorrectAnswer("Language");
        quizFull.setCorrect(5);
        quizFull.setWrong(5);

        assertEquals(10, quizFull.getQuizID());
        assertEquals("Programming", quizFull.getCourse());
        assertEquals("What is Java?", quizFull.getQuizQuestion());
        assertEquals("Language", quizFull.getCorrectAnswer());
        assertEquals(5, quizFull.getCorrect());
        assertEquals(5, quizFull.getWrong());
    }

    // ---- Validation Tests ----
    @Test
    void testSettersRejectNullValues() {
        assertThrows(IllegalArgumentException.class, () -> quizFull.setQuizQuestion(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setAnswer1(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setAnswer2(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setAnswer3(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setAnswer4(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setCorrectAnswer(null));
        assertThrows(IllegalArgumentException.class, () -> quizFull.setQuizID(null));
    }

    // ---- Logic Tests ----
    @Test
    void testGetAverageWhenNoAttempts() {
        quizFull.setCorrect(0);
        quizFull.setWrong(0);
        assertEquals(0.0, quizFull.getAverage(), 0.001);
    }

    @Test
    void testGetAverageCorrectCalculation() {
        quizFull.setCorrect(3);
        quizFull.setWrong(1);
        assertEquals(75.0, quizFull.getAverage(), 0.001);
    }

    @Test
    void testToStringContainsKeyFields() {
        String str = quizFull.toString();
        assertTrue(str.contains("QuizQuestion"));
        assertTrue(str.contains("Answer1"));
        assertTrue(str.contains("correctAnswer"));
    }
}
