package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteQuizDAO implements IQuizDAO{
    private Connection connection;

    public SqliteQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        insertSampleData();
    }
    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quiz";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO quiz(user_id, Course, QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer) VALUES "
                    + "('1', 'CAB302',' put the matching input in', 'c', 'a', 'b', 'd', 'd'),"
                    + "('1', 'CAB302','put the non matching input in', 'a','b','c','b','b'),"
                    + "('1', 'CAB302','Is this loss', '|','||','||','|_','|_'),"
                    + "('1', 'CAB302','What is a valid type of signal modulation? ', 'Bell', 'Gate', 'Digitisation', 'Phase', 'Phase')";
//                    + "( 'Fourier transform of a Gate function? ', 'Sin', 'Cos', 'Sinc', 'Cot', 'Sinc','CAB302'),"
//                    + "( 'What Filter is used in FM signal reception', 'Matched', 'Bandpass', 'Lowpass', 'IDK','Matched','CAB302'),"
//                    + "('What are diodes made of?', 'Doped silicon', 'Doped lemon', 'Spicy rocks?', 'Copper', 'Doped silicon','CAB302'),"
//                    + "('What isn't a Diode?', 'LED', 'Zener', 'Fast Switch', 'BJT', 'BJT','CAB302'),"
//                    + "('Which is a lowpass active filter?', 'Weber', 'Butterworth', 'XMT', 'Winebago', 'Butterworth','CAB302')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quiz";
            clearStatement.execute(clearQuery);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quiz("
                    + "quizID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "user_id INTEGER NOT NULL"
                    + "course_id INTEGER,"
                    + "QuizQuestion VARCHAR NOT NULL,"
                    + "Answer1 VARCHAR NOT NULL,"
                    + "Answer2 VARCHAR NOT NULL,"
                    + "Answer3 VARCHAR NOT NULL,"
                    + "Answer4 VARCHAR NOT NULL,"
                    + "correctAnswer VARCHAR NOT NULL,"
                    + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY(course_id) REFERENCES courses(id) ON DELETE CASCADE,"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quiz (user_id, Course, QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, quiz.getUser().getId());
            statement.setInt(2, quiz.getCourse().getId());
            statement.setString(3, quiz.getQuizQuestion());
            statement.setString(4, quiz.getAnswer1());
            statement.setString(5, quiz.getAnswer2());
            statement.setString(6, quiz.getAnswer3());
            statement.setString(7, quiz.getAnswer4());
            statement.setString(8, quiz.getCorrectAnswer());
            statement.executeUpdate();
            // Set the id of the new contact
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                quiz.setQuizID(generatedKeys.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateQuiz(Quiz quizs) {

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE quiz SET Course = ?, QuizQuestion = ?, Answer1 = ?, Answer2 = ?, Answer3 = ?, Answer4 = ?, correctAnswer = ? WHERE QuizID = ?");
            statement.setInt(1, quizs.getCourse().getId());
            statement.setString(2, quizs.getQuizQuestion());
            statement.setString(3, quizs.getAnswer1());
            statement.setString(4, quizs.getAnswer2());
            statement.setString(5, quizs.getAnswer3());
            statement.setString(6, quizs.getAnswer4());
            statement.setString(7, quizs.getCorrectAnswer());
            statement.setInt(8, quizs.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuiz(Quiz quizs) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM quiz WHERE QuizID = ?");
            statement.setInt(1, quizs.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Quiz getQuiz(int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM quiz WHERE QuizID = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                int user_id = resultSet.getInt("user_id");
                int course_id = resultSet.getInt("Course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");

                //Fetching the user from the database
                IUserDAO userDAO = new SqliteUserDAO();
                User user = userDAO.getUser(user_id);

                //Fetching the Course from the database
                ICoursesDAO courseDAO = new SqliteCoursesDAO();
                Course course = courseDAO.getCourse(course_id);

                Quiz quizs = new Quiz(user, course,question, answer1,answer2,answer3, answer4, correctAnswer);
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
            String query = "SELECT * FROM quiz";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                int id = resultSet.getInt("QuizID");
                int user_id = resultSet.getInt("user_id");
                int course_id = resultSet.getInt("Course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");

                //Fetching the user from the database
                IUserDAO userDAO = new SqliteUserDAO();
                User user = userDAO.getUser(user_id);

                //Fetching the Course from the database
                ICoursesDAO courseDAO = new SqliteCoursesDAO();
                Course course = courseDAO.getCourse(course_id);

                Quiz quiz = new Quiz(user, course, question, answer1, answer2, answer3, answer4, correctAnswer);
                quiz.setQuizID(id);
                quizs.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizs;
    }



    @Override
    public List<Quiz> getAllQuestionsfromCourse(Course course) {
        List<Quiz> quizs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement( "SELECT * FROM quiz WHERE Course = ?");
            statement.setInt(1,course.getId());
            ResultSet resultSet = statement.executeQuery();
            System.out.println("at query");
            while (resultSet.next()) {

                int id = resultSet.getInt("QuizID");
                int user_id = resultSet.getInt("user_id");
                //String course = resultSet.getString("course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");

                //Fetching the user from the database
                IUserDAO userDAO = new SqliteUserDAO();
                User user = userDAO.getUser(user_id);

                Quiz quiz = new Quiz(user, course, question, answer1,answer2,answer3, answer4, correctAnswer);
                quiz.setQuizID(id);
                quizs.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(quizs);
        return quizs;
    }
}
