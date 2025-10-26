package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Score;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite implementation of {@link IScoresDAO}.
 * <p>
 * Handles all database operations related to student scores, including
 * creation, retrieval, updates, deletions, and sample data initialization.
 */
public class SqliteScoreDAO implements IScoresDAO {

    /** Active database connection. */
    private final Connection connection;

    /**
     * Initializes the DAO, creates the scores table if needed,
     * and inserts sample data if the table is empty.
     */
    public SqliteScoreDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        if (getAllScores().isEmpty()) {
            insertSampleData();
        }
    }

    // Creates the scores table if it does not already exist
    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "stdntID INTEGER NOT NULL," +
                    "quizScore REAL NOT NULL," +
                    "pvpScore REAL NOT NULL," +
                    "pvpBattle INTEGER DEFAULT 0," +
                    "quizAttempts INTEGER DEFAULT 0" +
                    ")";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addScore(Score score) {
        String query = "INSERT INTO scores (stdntID, quizScore, pvpScore, pvpBattle, quizAttempts) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, score.getStudentID());
            ps.setDouble(2, score.getQuizScore());
            ps.setDouble(3, score.getPvpScore());
            ps.setInt(4, score.getPvpBattle());
            ps.setInt(5, score.getQuizAttempts());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateScore(Score score) {
        String query = "UPDATE scores SET quizScore = ?, pvpScore = ?, pvpBattle = ?, quizAttempts = ? WHERE stdntID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, score.getQuizScore());
            ps.setDouble(2, score.getPvpScore());
            ps.setInt(3, score.getPvpBattle());
            ps.setInt(4, score.getQuizAttempts());
            ps.setInt(5, score.getStudentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteScore(Score score) {
        String query = "DELETE FROM scores WHERE stdntID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, score.getStudentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Score getScore(int id) {
        String query = "SELECT * FROM scores WHERE stdntID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Score(
                        rs.getInt("stdntID"),
                        rs.getDouble("quizScore"),
                        rs.getDouble("pvpScore"),
                        rs.getInt("pvpBattle"),
                        rs.getInt("quizAttempts")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

    /** {@inheritDoc} */
    @Override
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM scores";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("stdntID"),
                        rs.getDouble("quizScore"),
                        rs.getDouble("pvpScore"),
                        rs.getInt("pvpBattle"),
                        rs.getInt("quizAttempts")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    /** {@inheritDoc} */
    @Override
    public void clearData() {
        String query = "DELETE FROM scores";
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void insertSampleData() {
        clearData();
        addScore(new Score(101, 0.75, 0.82, 5, 3));
        addScore(new Score(102, 0.68, 0.70, 4, 2));
        addScore(new Score(103, 0.90, 0.88, 7, 5));
    }

    /**
     * Updates a student's PvP score after a match.
     *
     * @param stdntID student ID
     * @param result  result of match (1 = win, 0 = loss)
     */
    public void updatePVPScore(int stdntID, int result) {
        try {
            Score score = getScore(stdntID);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE scores SET pvpScore = ?, pvpBattle = ? WHERE stdntID = ?");
            statement.setDouble(1, (score.getPvpScore() + result) / 2);
            statement.setDouble(2, score.getPvpBattle() + 1);
            statement.setInt(3, stdntID);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a student's quiz score and increments their attempt count.
     *
     * @param stdntID        student ID
     * @param newQuizResult  latest quiz result (0–1)
     */
    public void updateQuizScore(int stdntID, double newQuizResult) {
        try {
            Score score = getScore(stdntID);
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE scores SET quizScore = ?, quizAttempts = ? WHERE stdntID = ?");
            statement.setDouble(1, (score.getQuizScore() + newQuizResult) / 2);
            statement.setDouble(2, score.getQuizAttempts() + 1);
            statement.setInt(3, stdntID);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
