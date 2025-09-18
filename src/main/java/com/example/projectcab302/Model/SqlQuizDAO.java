package com.example.projectcab302.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlQuizDAO implements IQuizDAO{
    private Connection connection;

    public SqlQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Used for testing, to be removed later
        insertSampleData();
    }
    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quizs";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO quizs(QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer) VALUES "
                    + "(' put the matching input in', 'c', 'a', 'b', 'd', 'd'),"
                    + "('put the non matching input in', 'a','b','c','b','b'),"
                    + "('Is this loss', '|','||','||','|_','|_'),"
                    + "('What is a valid type of signal modulation? ', 'Bell', 'Gate', 'Digitisation', 'Phase', 'Phase'),"
                    + "( 'Fourier transform of a Gate function? ', 'Sin', 'Cos', 'Sinc', 'Cot', 'Sinc'),"
                    + "( 'What Filter is used in FM signal reception', '', 'Matched', 'Bandpass', 'Lowpass', 'IDK','Matched'),"
                    + "('What are diodes made of?', 'Doped silicon', 'Doped lemon', 'Spicy rocks?', 'Copper', 'Doped silicon'),"
                    + "('What isn't a Diode?', 'LED', 'Zener', 'Fast Switch', 'BJT', 'BJT'),"
                    + "('Which is a lowpass active filter?', 'Weber', 'Butterworth', 'XMT', 'Winebago', 'Butterworth')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quizs";
            clearStatement.execute(clearQuery);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizs ("
                    + "quizID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "QuizQuestion VARCHAR NOT NULL,"
                    + "Answer1 VARCHAR NOT NULL,"
                    + "Answer2 VARCHAR NOT NULL,"
                    + "Answer3 VARCHAR NOT NULL,"
                    + "Answer4 VARCHAR NOT NULL,"
                    + "correctAnswer VARCHAR NOT NULL"
                    // + ", course VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addQuiz(Quiz quizs) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quizs (QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, quizs.getQuizQuestion());
            statement.setString(2, quizs.getAnswer1());
            statement.setString(3, quizs.getAnswer2());
            statement.setString(4, quizs.getAnswer3());
            statement.setString(5, quizs.getAnswer4());
            statement.setString(6, quizs.getCorrectAnswer());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quizs.setQuizID(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateQuiz(Quiz quizs) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE flashcards SET QuizQuestion = ?, Answer1 = ?, Answer2 = ?, Answer3 = ?, Answer4 = ?, correctAnswer = ?, WHERE QuizID = ?");
            statement.setString(1, quizs.getQuizQuestion());
            statement.setString(2, quizs.getAnswer1());
            statement.setString(3, quizs.getAnswer2());
            statement.setString(4, quizs.getAnswer3());
            statement.setString(5, quizs.getAnswer4());
            statement.setString(6, quizs.getCorrectAnswer());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuiz(Quiz quizs) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quizs WHERE QuizID = ?");
            statement.setInt(1, quizs.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Quiz getQuiz(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quizs WHERE QuizID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                //String course = resultSet.getString("course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");

                Quiz quizs = new Quiz(question, answer1,answer2,answer3, answer4, correctAnswer);
                quizs.setQuizID(id);
                return quizs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public List<Quiz> getAllQuizs() {
        List<Quiz> quizs = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM quizs";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                int id = resultSet.getInt("QuizID");
                //String course = resultSet.getString("course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");
                Quiz quiz = new Quiz(question, answer1,answer2,answer3, answer4, correctAnswer);
                quiz.setQuizID(id);
                quizs.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizs;
    }
}
