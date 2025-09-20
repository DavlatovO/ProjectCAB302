package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteCoursesDAO implements ICoursesDAO{
    private Connection connection;

    public SqliteCoursesDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        // Used for testing, to be removed later

    }

    @Override
    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM courses";
            clearStatement.execute(clearQuery);
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


    @Override
    public void clearData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM courses";
            clearStatement.execute(clearQuery);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Boolean checkTitleExists(String title) {
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE title = ?");
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTable() {
        // Create table if not exists
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



    @Override
    public void addCourse(Course course) {
        if (this.checkTitleExists(course.getTitle())){
            throw new IllegalArgumentException("Course with this name already exists");
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (title) VALUES (?)");
            statement.setString(1, course.getTitle());

            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                course.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE courses SET title = ? WHERE id = ?");
            statement.setString(1, course.getTitle());
            statement.setInt(2, course.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCourse(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM courses WHERE id = ?");
            statement.setInt(1, course.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course getCourse(int id) {

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses WHERE id = ?");
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
                Course course= new Course(title);
                course.setId(id);
                courses.add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }




}
