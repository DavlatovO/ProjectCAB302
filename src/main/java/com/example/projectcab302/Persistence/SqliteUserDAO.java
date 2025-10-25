package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Score;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.Utils.Hashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteUserDAO implements IUserDAO {
    private Connection connection;

    public SqliteUserDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData();
    }

    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM users";
            clearStatement.execute(clearQuery);

            // Insert sample users
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO users(id, username, email, role, password) VALUES "
                    //+ "(1, 'bex', 'bex@gmail.com', 'Student', '123'),"
                    + "(101, 'Alice', 'Alice@gmail.com', 'Student', '123'),"
                    + "(102, 'Bob', 'Bob@gmail.com', 'Student', '123'),"
                    + "(103, 'Charlie', 'Charlie@gmail.com', 'Student', '123'),"
                    + "(2, 'Sean', 'sean@gmail.com', 'Teacher', '123')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM users";
            clearStatement.execute(clearQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "role TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, email, role, password) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getHashedPassword());
            statement.executeUpdate();

            // Retrieve generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

            if (user.getRole().equals("Student")){
                IScoresDAO scoresDAO = new SqliteScoreDAO();
                Score score = new Score(user.getId(), 0, 0, 0, 0);
                scoresDAO.addScore(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET username = ?, email = ?, role = ?, password = ? WHERE id = ?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRoles().name());
            statement.setString(4, user.getHashedPassword());
            statement.setInt(5, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String username, String plainPassword) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, role, email, password FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String roleStr = resultSet.getString("role");
                String email = resultSet.getString("email");
                String storedHash = resultSet.getString("password");

                String inputHash = Hashing.hashPassword(plainPassword);

                if (storedHash.equals(inputHash)) {
                    User.Roles roleEnum = User.Roles.valueOf(roleStr);
                    User user = switch (roleEnum) {
                        case Student -> new Student(username, email, roleEnum, storedHash);
                        case Teacher -> new Teacher(username, email, roleEnum, storedHash);
                    };
                    user.setId(id);
                    return user;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, username, email, password FROM users WHERE role = 'Student'"
            );
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Student student = new Student(username, email, User.Roles.Student, password);
                student.setId(id);
                students.add(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public boolean emailExists(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Student getStudent(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, username, email, role, password FROM users WHERE id = ? AND role = 'Student'"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                Student student = new Student(username, email, User.Roles.Student, password);
                student.setId(id);
                return student;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
