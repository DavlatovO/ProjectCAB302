package com.example.projectcab302.Model;

import java.util.List;


/**
 * Interface for the Contact Data Access Object that handles
 * the CRUD operations for the Contact class with the database.
 */
public interface IFlashcardDAO {
    /**
     * Adds a new contact to the database.
     * @param flashcard The contact to add.
     */
    public void addFlashcard(Flashcard flashcard);
    /**
     * Updates an existing contact in the database.
     * @param flashcard The contact to update.
     */
    public void updateFlashcard(Flashcard flashcard);
    /**
     * Deletes a contact from the database.
     * @param flashcard The contact to delete.
     */
    public void deleteFlashcard(Flashcard flashcard);
    /**
     * Retrieves a contact from the database.
     * @param id The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Flashcard getFlashcard(int id);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<Flashcard> getAllFlashcard();

    public void clearData();

    void insertSampleData();
}