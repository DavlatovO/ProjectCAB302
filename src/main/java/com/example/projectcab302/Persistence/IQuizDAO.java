package com.example.projectcab302.Persistence;


import com.example.projectcab302.Model.Quiz;

import java.util.List;

public interface IQuizDAO {
    public void addQuiz(Quiz quiz);

    public void deleteQuiz(Quiz quiz);
    /**
     * Retrieves a contact from the database.
     * @param QuizID The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Quiz getQuiz(int QuizID);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<Quiz> getAllQuizs();

    public void clearData();

    void insertSampleData();

    List<Quiz> getAllQuestionsfromCourse(String course);
}
