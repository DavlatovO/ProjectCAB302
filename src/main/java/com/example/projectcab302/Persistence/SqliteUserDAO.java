package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.Utils.Hashing;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class SqliteUserDAO implements IUserDAO {
    private Connection connection;

    public SqliteUserDAO() {
        this.connection = SqliteConnection.getInstance();
        createTable();
    }

    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM users";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO users(username, email, role, password) VALUES "
                    + "('bex', 'bex@gmail.com', 'Student', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3')"
                    + "('Sean', 'sean@gmail.com', 'Teacher', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            // Clear before inserting
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
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "username TEXT UNIQUE NOT NULL,"
                    + "email TEXT UNIQUE NOT NULL,"
                    + "role TEXT NOT NULL,"
                    + "password TEXT NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(User user) {
        //Save user details to the database
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, email, role, password) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS // <-- telling JDBC to return the id
            );

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getHashedPassword());

            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET username = ?, email = ?, role = ?, password = ? WHERE id = ?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getRoles().name());
            statement.setString(4, user.getHashedPassword());
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
    public  User login(String username, String plainPassword) {
        //Checking user details to login
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id, role, email, password FROM users WHERE username = ?");
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
                        case Teacher -> new Teacher(username, email, roleEnum, storedHash );
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
    public User getUser(int user_id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT FROM user WHERE id = ?");
            statement.setInt(1, user_id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                String password = resultSet.getString("password");

                //getting user role as enum
                User.Roles roleEnum = User.Roles.valueOf(role);

                User user = switch (roleEnum) {
                    case Student -> new Student(username, email, roleEnum, password);
                    case Teacher -> new Teacher(username, email, roleEnum, password);
                };
                user.setId(user_id);
                return user;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean emailExists(String email) {
        //Retrieves true if email is already in the database
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
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








}

