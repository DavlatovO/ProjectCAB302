package com.example.projectcab302.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlQuizDAO {
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
            String insertQuery = "INSERT INTO quizs(QuizName, QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer, Course\n) VALUES "
                    + "('bad inputs',' put the matching input in', 'c', 'a', 'b', 'd', 'd', 'CAB202'),"
                    + "('bad inputs', 'put the non matching input in', 'a','b','c','b','b', 'CAB202),"
                    + "('Memes', 'Is this loss', '|','||','||','|_','|_', 'CAB202'),"
                    + "('signals', 'What is a valid type of signal modulation? ', 'Bell', 'Gate', 'Digitisation', 'Phase', 'Phase', 'EGB342'),"
                    + "('signals', 'Fourier transform of a Gate function? ', 'Sin', 'Cos', 'Sinc', 'Cot', 'Sinc', 'EGB342'),"
                    + "('signals', 'What Filter is used in FM signal reception', '', 'Matched', 'Bandpass', 'Lowpass', 'IDK','Matched', 'EGB342'),"
                    + "('Diodes', 'What are diodes made of?', 'Doped silicon', 'Doped lemon', 'Spicy rocks?', 'Copper', 'Doped silicon', 'EGB348'),"
                    + "('Diodes', 'What isn't a Diode?', 'LED', 'Zener', 'Fast Switch', 'BJT', 'BJT', 'EGB348'),"
                    + "('Diodes', 'Which is a lowpass active filter?', 'Weber', 'Butterworth', 'XMT', 'Winebago', 'Butterworth', 'EGB348')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM flashcards";
            clearStatement.execute(clearQuery);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS flashcards ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "course VARCHAR NOT NULL,"
                    + "question VARCHAR NOT NULL,"
                    + "answer VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO flashcards (question, answer) VALUES (?, ?)");
            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());

            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                flashcard.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE flashcards SET question = ?, answer = ?, WHERE id = ?");
            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setInt(3, flashcard.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM flashcards WHERE id = ?");
            statement.setInt(1, flashcard.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Flashcard getFlashcard(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM flashcards WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String course = resultSet.getString("course");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                Flashcard flashcard = new Flashcard(question, answer, answer);
                flashcard.setId(id);
                return flashcard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Flashcard> getAllFlashcard() {
        List<Flashcard> flashcards = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM flashcards";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String course = resultSet.getString("course");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                Flashcard flashcard = new Flashcard(course, question, answer);
                flashcard.setId(id);
                flashcards.add(flashcard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flashcards;
    }
}
