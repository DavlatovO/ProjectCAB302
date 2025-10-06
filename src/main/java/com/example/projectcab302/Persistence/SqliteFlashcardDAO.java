package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link IFlashcardDAO} interface
 * that manages Flashcard entities in a SQLite database.
 * <p>
 * This class provides full CRUD operations and utilities
 * for managing flashcards, including sample data insertion
 * and clearing all stored flashcards.
 * </p>
 */
public class SqliteFlashcardDAO implements IFlashcardDAO {

    /**
     * The database connection used for executing SQL queries.
     */
    private Connection connection;

    /**
     * Constructs a new {@code SqliteFlashcardDAO} instance.
     * <p>
     * Establishes the database connection and ensures
     * the {@code flashcards} table exists.
     * </p>
     */
    public SqliteFlashcardDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Uncomment for testing: insertSampleData();
    }

    /**
     * Inserts predefined sample flashcards into the database.
     * <p>
     * This method clears the existing data before inserting
     * new sample records. Intended for testing or demonstration.
     * </p>
     */
    @Override
    public void insertSampleData() {
        try {
            // Clear existing data
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM flashcards";
            clearStatement.execute(clearQuery);

            // Insert sample flashcards
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO flashcards(user_id, course, question, answer) VALUES "
                    + "('1', 'CAB202', 'What does ldi do in AVR programming? ', 'load data into a register'),"
                    + "('1', 'CAB202', 'What does sts do in AVR programming? ', 'store data from a register to a data space'),"
                    + "('1', 'CAB202', 'What does rotate right do? ', 'Shifts all bits to the right and places the bits pushed out, back in front'),"
                    + "('1', 'CAB302', 'What is Java? ', 'Java is a statically typed, object-oriented programming language and a runtime platform (JVM).'),"
                    + "('1', 'CAB302', 'What agile software development? ', 'Agile software development is an iterative, incremental way of building software'),"
                    + "('1', 'CAB302', 'What is maven? ', 'A Java build automation and dependency management tool.'),"
                    + "('1', 'CAB201', 'In SOLID principles, what does the S stand for ', 'Single Responsibility, a class should have one reason to change'),"
                    + "('1', 'CAB201', 'In SOLID principles, what does the O stand for ', 'Open/closed, Software entities should be open for extension, closed for modification.'),"
                    + "('1', 'CAB201', 'In SOLID principles, what does the L stand for ', 'Liskov Substitution, subtypes must be usable wherever their base type is expected')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all flashcard records from the database.
     */
    @Override
    public void clearData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM flashcards";
            clearStatement.execute(clearQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the {@code flashcards} table in the database if it does not already exist.
     * <p>
     * The table has the following schema:
     * <ul>
     *     <li>{@code id} – INTEGER PRIMARY KEY AUTOINCREMENT</li>
     *     <li>{@code user_id} – INTEGER NOT NULL</li>
     *     <li>{@code course} – VARCHAR NOT NULL</li>
     *     <li>{@code question} – VARCHAR NOT NULL</li>
     *     <li>{@code answer} – VARCHAR NOT NULL</li>
     * </ul>
     * </p>
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS flashcards ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL,"
                    + "course VARCHAR NOT NULL,"
                    + "question VARCHAR NOT NULL,"
                    + "answer VARCHAR NOT NULL,"
                    + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new flashcard to the database.
     *
     * @param flashcard the {@link Flashcard} object to add
     */
    @Override
    public void addFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO flashcards (user_id, course, question, answer) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, flashcard.getUser().getId());
            statement.setInt(2, flashcard.getCourse().getId());
            statement.setString(3, flashcard.getQuestion());
            statement.setString(4, flashcard.getAnswer());

            statement.executeUpdate();

            // Retrieve and assign the generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                flashcard.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing flashcard in the database.
     *
     * @param flashcard the {@link Flashcard} object with updated question and answer
     */
    @Override
    public void updateFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE flashcards SET question = ?, answer = ? WHERE id = ?");
            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setInt(3, flashcard.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a flashcard from the database.
     *
     * @param flashcard the {@link Flashcard} object to delete
     */
    @Override
    public void deleteFlashcard(Flashcard flashcard) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM flashcards WHERE id = ?");
            statement.setInt(1, flashcard.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a flashcard by its ID.
     *
     * @param id the ID of the flashcard to retrieve
     * @return a {@link Flashcard} object if found, otherwise {@code null}
     */
    @Override
    public Flashcard getFlashcard(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM flashcards WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int course_id = resultSet.getInt("course");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                int user_id = resultSet.getInt("user_id");

                //Fetching the user from db
                IUserDAO userDAO = new SqliteUserDAO();
                User user = userDAO.getUser(user_id);

                //Fetching the Course from the database
                ICoursesDAO courseDAO = new SqliteCoursesDAO();
                Course course = courseDAO.getCourse(course_id);

                Flashcard flashcard = new Flashcard(user, course, question, answer);
                flashcard.setId(id);
                return flashcard;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all flashcards stored in the database.
     *
     * @return a list of all {@link Flashcard} objects
     */
    @Override
    public List<Flashcard> getAllFlashcard() {
        List<Flashcard> flashcards = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM flashcards";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int course_id = resultSet.getInt("course");
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                int user_id = resultSet.getInt("user_id");

                //Fetching the user from the database
                IUserDAO userDAO = new SqliteUserDAO();
                User user = userDAO.getUser(user_id);

                //Fetching the Course from the database
                ICoursesDAO courseDAO = new SqliteCoursesDAO();
                Course course = courseDAO.getCourse(course_id);

                Flashcard flashcard = new Flashcard(user, course, question, answer);
                flashcard.setId(id);
                flashcards.add(flashcard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flashcards;
    }
}
