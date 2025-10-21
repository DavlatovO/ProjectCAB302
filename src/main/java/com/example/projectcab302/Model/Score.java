package com.example.projectcab302.Model;

public class Score {
    private int scoreID;
    private int studentID;
    private double quizScore;
    private double pvpScore;

    // Constructor
    public Score(int studentID, double quizScore, double pvpScore) {
        this.studentID = studentID;
        this.quizScore = quizScore;
        this.pvpScore = pvpScore;
    }

    // Getters
    public int getStudentID() {
        return studentID;
    }

    public double getQuizScore() {
        return quizScore;
    }

    public double getPvpScore() {
        return pvpScore;
    }

    public int getScoreID() {return scoreID;}

    // Setters
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    public void setPvpScore(double pvpScore) {
        this.pvpScore = pvpScore;
    }

    public void setScoreID(int scoreID) { this.scoreID = scoreID; }

    // Optional helper: calculate overall average
    public double getAverageScore() {
        return (quizScore + pvpScore) / 2.0;
    }

    // toString() for debugging or logging
    @Override
    public String toString() {
        return "Score{" +
                "studentID=" + studentID +
                ", quizScore=" + quizScore +
                ", pvpScore=" + pvpScore +
                ", averageScore=" + getAverageScore() +
                '}';
    }
}

