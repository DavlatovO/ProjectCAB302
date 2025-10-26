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

/**
 * SQLite implementation of {@link IUserDAO}.
 * <p>
 * Manages database operations for user accounts including creation,
 * retrieval, authentication, and sample data population.
 */
public class SqliteUserDAO implements IUserDAO {

    /** Active database connection. */
    private Connection connection;

    /**
     * Initializes the DAO, creates the users table if necessary,
     * and inserts sample data if the table is empty.
     */
    public SqliteUserDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
        if (getAllUsers().isEmpty()) {
            insertSampleData();
        }
    }

    // Inserts initial sample data for testing
    public void insertSampleData() {
        try {
            // Clear existing data
            Statement clearStatement = connection.createStatement();
            clearStatement.execute("DELETE FROM users");

            // Insert demo users
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO users(id, username, email, role, password) VALUES "
                    + "(101, 'Alice', 'Alice@gmail.com', 'Student', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),"
                    + "(102, 'Bob', 'Bob@gmail.com', 'Student', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),"
                    + "(103, 'Charlie', 'Charlie@gmail.com', 'Student', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),"
                    + "(2, 'Sean', 'sean@gmail.com', 'Teacher', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, username, email, role, password FROM users");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String roleStr = rs.getString("role");
                String password = rs.getString("password");

                User.Roles roleEnum = User.Roles.valueOf(roleStr);
                User user = switch (roleEnum) {
                    case Student -> new Student(username, email, roleEnum, password);
                    case Teacher -> new Teacher(username, email, roleEnum, password);
                };

                user.setId(id);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // Clears all user data (used for testing or resets)
    public void clearData() {
        try {
            Statement clearStatement = connection.createStatement();
            clearStatement.execute("DELETE FROM users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Creates the users table if it does not already exist
    private void createTable() {
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

    /** {@inheritDoc} */
    @Override
    public void createUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, email, role, password) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRoles().name());
            statement.setString(4, user.getHashedPassword());
            statement.executeUpdate();

            // Assign auto-generated ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }

            // Automatically create score entry for new students
            if (user.getRoles().name().equals("Student")) {
                IScoresDAO scoresDAO = new SqliteScoreDAO();
                Score score = new Score(user.getId(), 1, 1, 0, 0);
                scoresDAO.addScore(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing user's details.
     *
     * @param user the user with updated information
     */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public User login(String username, String plainPassword) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, role, email, password FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String roleStr = rs.getString("role");
                String email = rs.getString("email");
                String storedHash = rs.getString("password");
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
        return null; // login failed
    }

    /** {@inheritDoc} */
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, username, email, password FROM users WHERE role = 'Student'");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");

                Student student = new Student(username, email, User.Roles.Student, password);
                student.setId(id);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /** {@inheritDoc} */
    @Override
    public boolean emailExists(String email) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE email = ?");
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Student getStudent(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, username, email, role, password FROM users WHERE id = ? AND role = 'Student'");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");

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
