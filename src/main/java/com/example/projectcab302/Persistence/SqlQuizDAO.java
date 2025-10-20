package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlQuizDAO implements IQuizDAO{
    private Connection connection;

    public SqlQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData();
    }
    public void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quiz";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO quiz(Course, QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer, correct, wrong) VALUES "
                    + "('CAB302',' put the matching input in', 'c', 'a', 'b', 'd', 'd', 5, 7),"
                    + "('CAB302','put the non matching input in', 'a','b','c','b','b', 3, 9),"
                    + "('CAB302','Is this loss', '|','||','||','|_','|_', 12, 0),"
                    + "('CAB302','What is a valid type of signal modulation? ', 'Bell', 'Gate', 'Digitisation', 'Phase', 'Phase', 6, 6)";
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
                    + "Course VARCHAR NOT NULL,"
                    + "QuizQuestion VARCHAR NOT NULL,"
                    + "Answer1 VARCHAR NOT NULL,"
                    + "Answer2 VARCHAR NOT NULL,"
                    + "Answer3 VARCHAR NOT NULL,"
                    + "Answer4 VARCHAR NOT NULL,"
                    + "correctAnswer VARCHAR NOT NULL,"
                    + "correct INTEGER NOT NULL,"
                    + "wrong INTEGER NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addQuiz(Quiz quiz) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO quiz (Course, QuizQuestion, Answer1, Answer2, Answer3, Answer4, correctAnswer) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, quiz.getCourse());
            statement.setString(2, quiz.getQuizQuestion());
            statement.setString(3, quiz.getAnswer1());
            statement.setString(4, quiz.getAnswer2());
            statement.setString(5, quiz.getAnswer3());
            statement.setString(6, quiz.getAnswer4());
            statement.setString(7, quiz.getCorrectAnswer());
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
            statement.setString(1, quizs.getCourse());
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

                String course = resultSet.getString("Course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");
                int correct = resultSet.getInt("correct");
                int wrong = resultSet.getInt("wrong");

                Quiz quizs = new Quiz(course,question, answer1,answer2,answer3, answer4, correctAnswer, correct, wrong);
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
                String course = resultSet.getString("Course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");
                int correct = resultSet.getInt("correct");
                int wrong = resultSet.getInt("wrong");
                Quiz quiz = new Quiz(course, question, answer1, answer2, answer3, answer4, correctAnswer, correct, wrong);
                quiz.setQuizID(id);
                quizs.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizs;
    }



    @Override
    public List<Quiz> getAllQuestionsfromCourse(String course) {
        List<Quiz> quizs = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement( "SELECT * FROM quiz WHERE Course = ?");
            statement.setString(1,course);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("at query");
            while (resultSet.next()) {

                int id = resultSet.getInt("QuizID");
                //String course = resultSet.getString("course");
                String question = resultSet.getString("QuizQuestion");
                String answer1 = resultSet.getString("Answer1");
                String answer2 = resultSet.getString("Answer2");
                String answer3 = resultSet.getString("Answer3");
                String answer4 = resultSet.getString("Answer4");
                String correctAnswer = resultSet.getString("correctAnswer");
                int correct = resultSet.getInt("correct");
                int wrong = resultSet.getInt("wrong");
                Quiz quiz = new Quiz(course, question, answer1,answer2,answer3, answer4, correctAnswer, correct, wrong);
                quiz.setQuizID(id);
                quizs.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(quizs);
        return quizs;
    }

    @Override
    public void updateQuizMetrics(Quiz quizs, Boolean result) {

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE quiz SET correct = ?, wrong = ? WHERE QuizID = ?");
            if (result == Boolean.TRUE) {
                statement.setInt(1, quizs.getCorrect() + 1);
                statement.setInt( 2, quizs.getWrong());
            }
            if (result == Boolean.FALSE) {
                statement.setInt(1, quizs.getCorrect() );
                statement.setInt( 2, quizs.getWrong() + 1);
            }
            statement.setInt(3, quizs.getQuizID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
