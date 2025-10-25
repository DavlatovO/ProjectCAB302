package com.example.projectcab302.Model;

/**
 * Represents a student's performance metrics in both quizzes and PvP battles.
 * <p>
 * Each {@code Score} instance stores a student's unique ID, quiz and PvP scores,
 * as well as the number of attempts and battles completed. This class provides
 * methods to update and compute aggregated performance statistics.
 * </p>
 */
public class Score {

    /** The student's unique identifier. */
    private int studentID;

    /** The student's average quiz score. */
    private double quizScore;

    /** The student's average PvP score. */
    private double pvpScore;

    /** The total number of PvP battles attempted. */
    private int pvpBattle;

    /** The total number of quiz attempts made. */
    private int quizAttempts;

    // ─────────────────────────────
    // Constructors
    // ─────────────────────────────

    /**
     * Constructs a new {@code Score} with full details.
     * <p>
     * Typically used when reading an existing record from the database.
     * </p>
     *
     * @param studentID    the student's unique ID
     * @param quizScore    the student's average quiz score
     * @param pvpScore     the student's average PvP score
     * @param pvpBattle    the number of PvP battles attempted
     * @param quizAttempts the number of quiz attempts made
     */
    public Score(int studentID, double quizScore, double pvpScore, int pvpBattle, int quizAttempts) {
        this.studentID = studentID;
        this.quizScore = quizScore;
        this.pvpScore = pvpScore;
        this.pvpBattle = pvpBattle;
        this.quizAttempts = quizAttempts;
    }

    /**
     * Constructs a new {@code Score} with only basic performance data.
     * <p>
     * This constructor is typically used when inserting a new student score record.
     * The number of PvP battles and quiz attempts is initialized to zero.
     * </p>
     *
     * @param studentID the student's unique ID
     * @param quizScore the student's initial quiz score
     * @param pvpScore  the student's initial PvP score
     */
    public Score(int studentID, double quizScore, double pvpScore) {
        this(studentID, quizScore, pvpScore, 0, 0);
    }

    // ─────────────────────────────
    // Getters
    // ─────────────────────────────

    /**
     * Returns the student's unique ID.
     *
     * @return the student ID
     */
    public int getStudentID() {
        return studentID;
    }

    /**
     * Returns the student's quiz score.
     *
     * @return the quiz score
     */
    public double getQuizScore() {
        return quizScore;
    }

    /**
     * Returns the student's PvP score.
     *
     * @return the PvP score
     */
    public double getPvpScore() {
        return pvpScore;
    }

    /**
     * Returns the number of PvP battles attempted.
     *
     * @return the number of PvP battles
     */
    public int getPvpBattle() {
        return pvpBattle;
    }

    /**
     * Returns the number of quiz attempts made.
     *
     * @return the number of quiz attempts
     */
    public int getQuizAttempts() {
        return quizAttempts;
    }

    // ─────────────────────────────
    // Setters
    // ─────────────────────────────

    /**
     * Sets the student ID.
     *
     * @param studentID the student’s unique identifier
     */
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    /**
     * Sets the student's quiz score.
     *
     * @param quizScore the quiz score to set
     */
    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    /**
     * Sets the student's PvP score.
     *
     * @param pvpScore the PvP score to set
     */
    public void setPvpScore(double pvpScore) {
        this.pvpScore = pvpScore;
    }

    /**
     * Sets the number of PvP battles attempted.
     *
     * @param pvpBattle the number of PvP battles
     */
    public void setPvpBattle(int pvpBattle) {
        this.pvpBattle = pvpBattle;
    }

    /**
     * Sets the number of quiz attempts made.
     *
     * @param quizAttempts the number of quiz attempts
     */
    public void setQuizAttempts(int quizAttempts) {
        this.quizAttempts = quizAttempts;
    }

    // ─────────────────────────────
    // Helpers
    // ─────────────────────────────

    /**
     * Calculates the average of the quiz and PvP scores.
     *
     * @return the overall average score
     */
    public double getAverageScore() {
        return (quizScore + pvpScore) / 2.0;
    }

    /**
     * Increments the number of PvP battles by one.
     */
    public void incrementPvpBattle() {
        this.pvpBattle++;
    }

    /**
     * Increments the number of quiz attempts by one.
     */
    public void incrementQuizAttempts() {
        this.quizAttempts++;
    }

    // ─────────────────────────────
    // Object Overrides
    // ─────────────────────────────

    /**
     * Returns a string representation of this {@code Score} instance,
     * including the student's ID, scores, and averages.
     *
     * @return a formatted string containing score details
     */
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
