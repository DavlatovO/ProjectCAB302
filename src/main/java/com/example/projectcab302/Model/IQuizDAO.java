package com.example.projectcab302.Model;


import java.util.List;

public interface IQuizDAO {
    public void addQuiz(Quiz quiz);

    public void deleteQuiz(Quiz quiz);
    /**
     * Retrieves a contact from the database.
     * @param id The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Quiz getFlashcard(int quizID);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<Flashcard> getAllFlashcard();

    public void clearData();

    void insertSampleData();
}
