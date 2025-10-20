package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Submission;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteSubmissionDAO implements ISubmissionDAO {
    private Connection connection;

    public SqliteSubmissionDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = """
                CREATE TABLE IF NOT EXISTS submissions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER NOT NULL,
                    courseName TEXT NOT NULL,
                    correctAnswerIDs TEXT,
                    incorrectAnswerIDs TEXT,
                    score REAL,
                    FOREIGN KEY(userId) REFERENCES users(id)
                )
                """;
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSubmission(Submission submission) {
        try {
            PreparedStatement stmt = connection.prepareStatement("""
                INSERT INTO submissions (userId, courseName, correctAnswerIDs, incorrectAnswerIDs, score)
                VALUES (?, ?, ?, ?, ?)
            """);

            stmt.setInt(1, submission.getUserId());
            stmt.setString(2, submission.getCourseName());
            stmt.setString(3, listToString(submission.getCorrectAnswerIDs()));
            stmt.setString(4, listToString(submission.getIncorrectAnswerIDs()));
            stmt.setDouble(5, submission.getScore());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Submission getSubmissionById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM submissions WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapResultSetToSubmission(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Submission> getSubmissionsByUser(int userId) {
        List<Submission> submissions = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM submissions WHERE userId = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) submissions.add(mapResultSetToSubmission(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    @Override
    public List<Submission> getSubmissionsByCourse(String courseName) {
        List<Submission> submissions = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM submissions WHERE courseName = ?");
            stmt.setString(1, courseName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) submissions.add(mapResultSetToSubmission(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    @Override
    public List<Submission> getAllSubmissions() {
        List<Submission> submissions = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM submissions");
            while (rs.next()) submissions.add(mapResultSetToSubmission(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    @Override
    public void deleteSubmission(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM submissions WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSubmissions() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM submissions");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === Utility Helpers ===

    private Submission mapResultSetToSubmission(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int userId = rs.getInt("userId");
        String courseName = rs.getString("courseName");
        List<Integer> correct = stringToList(rs.getString("correctAnswerIDs"));
        List<Integer> incorrect = stringToList(rs.getString("incorrectAnswerIDs"));
        double score = rs.getDouble("score");

        return new Submission(userId, courseName, correct, incorrect, score);
    }

    private String listToString(List<Integer> list) {
        return list.toString().replaceAll("[\\[\\] ]", ""); // "1,2,3"
    }

    private List<Integer> stringToList(String data) {
        List<Integer> list = new ArrayList<>();
        if (data == null || data.isEmpty()) return list;
        for (String s : data.split(",")) list.add(Integer.parseInt(s.trim()));
        return list;
    }
}
