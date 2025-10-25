package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link ICoursesDAO} interface
 * that manages Course entities in a SQLite database.
 * <p>
 * This class handles all CRUD (Create, Read, Update, Delete) operations
 * and provides utility methods such as clearing data, inserting sample data,
 * and checking if a course title already exists in the database.
 * </p>
 */
public class SqliteCoursesDAO implements ICoursesDAO {

    /**
     * The connection object for accessing the SQLite database.
     */
    private Connection connection;

    /**
     * Constructs a new {@code SqliteCoursesDAO} instance.
     * <p>
     * Initializes the database connection and ensures that the
     * required table is created.
     * </p>
     */
    public SqliteCoursesDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        if (getAllCourses().isEmpty()){
            insertSampleData();
        }
    }

    /**
     * Inserts a set of sample courses into the database.
     * <p>
     * This method first clears the existing courses table,
     * then inserts predefined course records for testing or demo purposes.
     * </p>
     */
    @Override
    public void insertSampleData() {
        try {
            // Clear existing records
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM courses";
            clearStatement.execute(clearQuery);

            // Insert sample records
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO courses(title) VALUES "
                    + "('CAB202'),"
                    + "('CAB302'),"
                    + "('CAB201')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all courses from the database.
     * <p>
     * This method clears the {@code courses} table.
     * </p>
     */
    @Override
    public void clearData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM courses";
            clearStatement.execute(clearQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether a course with the specified title already exists in the database.
     *
     * @param title the title of the course to check
     * @return {@code true} if a course with the specified title exists,
     *         {@code false} if it does not,
     *         or {@code null} if a database error occurs
     */
    @Override
    public Boolean checkTitleExists(String title) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM courses WHERE title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates the {@code courses} table in the database if it does not already exist.
     * <p>
     * The table contains the following columns:
     * <ul>
     *     <li>{@code id} - INTEGER PRIMARY KEY AUTOINCREMENT</li>
     *     <li>{@code title} - VARCHAR NOT NULL</li>
     * </ul>
     * </p>
     */
    private void createTable() {
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS courses ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "title VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new course to the database.
     *
     * @param course the {@link Course} object to be added
     * @throws IllegalArgumentException if a course with the same title already exists
     */
    @Override
    public void addCourse(Course course) {
        if (this.checkTitleExists(course.getTitle())) {
            throw new IllegalArgumentException("Course with this name already exists");
        }
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO courses (title) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, course.getTitle());

            statement.executeUpdate();

            // Set the generated ID on the course object
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                course.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing course in the database.
     *
     * @param course the {@link Course} object with updated information
     */
    @Override
    public void updateCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE courses SET title = ? WHERE id = ?");
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a course from the database.
     *
     * @param course the {@link Course} object to be deleted
     */
    @Override
    public void deleteCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM courses WHERE id = ?");
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a course by its ID from the database.
     *
     * @param id the ID of the course to retrieve
     * @return a {@link Course} object if found, or {@code null} if not found
     */
    @Override
    public Course getCourse(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM courses WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                Course course = new Course(title);
                course.setId(id);
                return course;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all courses from the database.
     *
     * @return a list of all {@link Course} objects stored in the database
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM courses";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");

                Course course = new Course(title);
                course.setId(id);
                courses.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }
}
