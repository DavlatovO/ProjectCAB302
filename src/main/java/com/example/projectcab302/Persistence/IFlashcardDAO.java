package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Flashcard;

import java.util.List;


/**
 * Interface for the flashcard Data Access Object that handles
 * the CRUD operations for the flashcard class with the database.
 */
public interface IFlashcardDAO {
    /**
     * Adds a new flashcard to the database.
     * @param flashcard The flashcard to add.
     */
    public void addFlashcard(Flashcard flashcard);
    /**
     * Updates an existing flashcard in the database.
     * @param flashcard The flashcard to update.
     */
    public void updateFlashcard(Flashcard flashcard);
    /**
     * Deletes a flashcard from the database.
     * @param flashcard The flashcard to delete.
     */
    public void deleteFlashcard(Flashcard flashcard);
    /**
     * Retrieves a flashcard from the database.
     * @param id The id of the flashcard to retrieve.
     * @return The flashcard with the given id, or null if not found.
     */
    public Flashcard getFlashcard(int id);
    /**
     * Retrieves all flashcard from the database.
     * @return A list of all flashcard in the database.
     */
    public List<Flashcard> getAllFlashcard();

    /**
     * Clears all data from the database.
     */
    public void clearData();

    /**
     * Inserts sample data in the data base
     */
    void insertSampleData();
}