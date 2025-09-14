package com.example.projectcab302.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteFlashcardDAO implements IFlashcardDAO{
    private Connection connection;

    public SqliteFlashcardDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Used for testing, to be removed later

    }


    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM flashcards";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO flashcards(question, answer) VALUES "
                    + "('What does ldi do in AVR programming? ', 'load data into a register'),"
                    + "('What does sts do in AVR programming? ', 'store data from a register to a data space'),"
                    + "('What does rotate right do? ', 'Shifts all bits to the right and places the bits pushed out, back in front')";
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
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                Flashcard flashcard = new Flashcard(question, answer);
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
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                Flashcard flashcard = new Flashcard(question, answer);
                flashcard.setId(id);
                flashcards.add(flashcard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flashcards;
    }
}
