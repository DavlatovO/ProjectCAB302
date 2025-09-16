package com.example.projectcab302.Model;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteQuizDAO {
    public static void main(String[] args) {
        Connection connection = SqliteConnection.getInstance();
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Quiz (id INTEGER PRIMARY KEY, QuizName VARCHAR, QuizQuestion VARCHAR, Answer1 VARCHAR, Answer2 VARCHAR, Answer3 VARCHAR, Answer4 VARCHAR, CorrectAnswer VARCHAR)");
            connection.close();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }
}