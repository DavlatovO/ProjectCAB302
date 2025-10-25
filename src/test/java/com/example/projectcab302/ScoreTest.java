package com.example.projectcab302.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score fullScore;
    private Score simpleScore;

    @BeforeEach
    void setUp() {
        fullScore = new Score(101, 85.0, 90.0, 3, 5);
        simpleScore = new Score(202, 70.0, 80.0);
    }

    // ─────────────────────────────
    // Constructor Tests
    // ─────────────────────────────
    @Test
    void testFullConstructorInitializesAllFields() {
        assertEquals(101, fullScore.getStudentID());
        assertEquals(85.0, fullScore.getQuizScore());
        assertEquals(90.0, fullScore.getPvpScore());
        assertEquals(3, fullScore.getPvpBattle());
        assertEquals(5, fullScore.getQuizAttempts());
    }

    @Test
    void testSimpleConstructorDefaultsToZeroCounts() {
        assertEquals(202, simpleScore.getStudentID());
        assertEquals(70.0, simpleScore.getQuizScore());
        assertEquals(80.0, simpleScore.getPvpScore());
        assertEquals(0, simpleScore.getPvpBattle());
        assertEquals(0, simpleScore.getQuizAttempts());
    }

    // ─────────────────────────────
    // Setter & Getter Tests
    // ─────────────────────────────
    @Test
    void testSettersUpdateValuesCorrectly() {
        fullScore.setStudentID(999);
        fullScore.setQuizScore(95.5);
        fullScore.setPvpScore(88.2);
        fullScore.setPvpBattle(7);
        fullScore.setQuizAttempts(9);

        assertEquals(999, fullScore.getStudentID());
        assertEquals(95.5, fullScore.getQuizScore());
        assertEquals(88.2, fullScore.getPvpScore());
        assertEquals(7, fullScore.getPvpBattle());
        assertEquals(9, fullScore.getQuizAttempts());
    }

    // ─────────────────────────────
    // Helper Method Tests
    // ─────────────────────────────
    @Test
    void testGetAverageScoreCalculation() {
        assertEquals((85.0 + 90.0) / 2.0, fullScore.getAverageScore(), 0.001);
        assertEquals((70.0 + 80.0) / 2.0, simpleScore.getAverageScore(), 0.001);
    }

    @Test
    void testIncrementPvpBattleIncreasesByOne() {
        int before = fullScore.getPvpBattle();
        fullScore.incrementPvpBattle();
        assertEquals(before + 1, fullScore.getPvpBattle());
    }

    @Test
    void testIncrementQuizAttemptsIncreasesByOne() {
        int before = fullScore.getQuizAttempts();
        fullScore.incrementQuizAttempts();
        assertEquals(before + 1, fullScore.getQuizAttempts());
    }

    // ─────────────────────────────
    // toString Tests
    // ─────────────────────────────
    @Test
    void testToStringContainsAllKeyFields() {
        String result = fullScore.toString();
        assertTrue(result.contains("studentID=101"));
        assertTrue(result.contains("quizScore=85.0"));
        assertTrue(result.contains("pvpScore=90.0"));
        assertTrue(result.contains("averageScore="));
    }
}
