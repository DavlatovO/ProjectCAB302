package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Score;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteScoreDAO implements IScoresDAO {

    private final Connection connection;

    public SqliteScoreDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        insertSampleData();
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "stdntID INTEGER NOT NULL," +
                    "quizScore REAL NOT NULL," +
                    "pvpScore REAL NOT NULL" +
                    ")";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addScore(Score score) {
        String query = "INSERT INTO scores (stdntID, quizScore, pvpScore) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, score.getStudentID());
            ps.setDouble(2, score.getQuizScore());
            ps.setDouble(3, score.getPvpScore());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                score.setScoreID(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//    public void addStudent(User user) {
//        String query = "INSERT INTO scores (stdntID, 0, 0) VALUES (?)";
//        try (PreparedStatement ps = connection.prepareStatement(query)) {
//            ps.setInt(1, user.getId());
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void updateScore(Score score) {
        String query = "UPDATE scores SET quizScore = ?, pvpScore = ? WHERE stdntID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDouble(1, score.getQuizScore());
            ps.setDouble(2, score.getPvpScore());
            ps.setInt(3, score.getStudentID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                        rs.getDouble("pvpScore")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // not found
    }

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
                        rs.getDouble("pvpScore")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    @Override
    public void clearData() {
        String query = "DELETE FROM scores";
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertSampleData() {
        clearData();
        addScore(new Score(101, 0.75, 0.82));
        addScore(new Score(102, 0.68, 0.70));
        addScore(new Score(103, 0.90, 0.88));
    }
}
