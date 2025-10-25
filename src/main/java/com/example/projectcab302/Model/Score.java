package com.example.projectcab302.Model;

public class Score {

    private int studentID;
    private double quizScore;
    private double pvpScore;
    private int pvpBattle;
    private int quizAttempts;

    // ─────────────────────────────
    // Constructors
    // ─────────────────────────────

    // Full constructor (used when reading from DB)
    public Score(int studentID, double quizScore, double pvpScore, int pvpBattle, int quizAttempts) {
        this.studentID = studentID;
        this.quizScore = quizScore;
        this.pvpScore = pvpScore;
        this.pvpBattle = pvpBattle;
        this.quizAttempts = quizAttempts;
    }

    // Simpler constructor (for initial inserts)
    public Score(int studentID, double quizScore, double pvpScore) {
        this(studentID, quizScore, pvpScore, 0, 0);
    }

    // ─────────────────────────────
    // Getters
    // ─────────────────────────────
    public int getStudentID() {
        return studentID;
    }

    public double getQuizScore() {
        return quizScore;
    }

    public double getPvpScore() {
        return pvpScore;
    }

    public int getPvpBattle() {
        return pvpBattle;
    }

    public int getQuizAttempts() {
        return quizAttempts;
    }

    // ─────────────────────────────
    // Setters
    // ─────────────────────────────
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    public void setPvpScore(double pvpScore) {
        this.pvpScore = pvpScore;
    }

    public void setPvpBattle(int pvpBattle) {
        this.pvpBattle = pvpBattle;
    }

    public void setQuizAttempts(int quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    // ─────────────────────────────
    // Helpers
    // ─────────────────────────────
    public double getAverageScore() {
        return (quizScore + pvpScore) / 2.0;
    }

    public void incrementPvpBattle() {
        this.pvpBattle++;
    }

    public void incrementQuizAttempts() {
        this.quizAttempts++;
    }

    // ─────────────────────────────
    // toString for debugging/logging
    // ─────────────────────────────
    @Override
    public String toString() {
        return "Score{" +
                "studentID=" + studentID +
                ", quizScore=" + quizScore +
                ", pvpScore=" + pvpScore +
                ", pvpBattle=" + pvpBattle +
                ", quizAttempts=" + quizAttempts +
                ", averageScore=" + getAverageScore() +
                '}';
    }
}
