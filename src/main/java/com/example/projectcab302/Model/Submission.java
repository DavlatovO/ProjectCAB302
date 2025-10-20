package com.example.projectcab302.Model;

import java.util.ArrayList;
import java.util.List;

public class Submission {
    private int userId;
    private String courseName;
    private List<Integer> correctAnswerIDs;
    private List<Integer> incorrectAnswerIDs;
    private double score;

    public Submission(int userId, String courseName, List<Integer> correctAnswerIDs, List<Integer> incorrectAnswerIDs, double score) {
        this.userId = userId;
        this.courseName = courseName;
        this.correctAnswerIDs = new ArrayList<>(correctAnswerIDs);
        this.incorrectAnswerIDs = new ArrayList<>(incorrectAnswerIDs);
        this.score = score;
    }

    // Alternate constructor that auto-calculates score
    public Submission(int userId, String courseName, List<Integer> correctAnswerIDs, List<Integer> incorrectAnswerIDs) {
        this(userId, courseName, correctAnswerIDs, incorrectAnswerIDs, 0.0);
        calculateScore();
    }

    private void calculateScore() {
        int total = correctAnswerIDs.size() + incorrectAnswerIDs.size();
        this.score = (total > 0) ? ((double) correctAnswerIDs.size() / total) : 0.0;
    }

    public int getUserId() { return userId; }
    public String getCourseName() { return courseName; }
    public List<Integer> getCorrectAnswerIDs() { return correctAnswerIDs; }
    public List<Integer> getIncorrectAnswerIDs() { return incorrectAnswerIDs; }
    public double getScore() { return score; }

    public String getSummary() {
        return String.format("Course %s (User %d) — Score: %.2f%% (%d/%d correct)",
                courseName, userId, score * 100,
                correctAnswerIDs.size(),
                correctAnswerIDs.size() + incorrectAnswerIDs.size());
    }
}
