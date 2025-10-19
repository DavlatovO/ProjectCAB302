package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Score;
import java.util.List;

public interface IScoresDAO {

    /**
     * Adds a new score to the database.
     * @param score The score to add.
     */
    void addScore(Score score);

    /**
     * Updates an existing score in the database.
     * @param score The score to update.
     */
    void updateScore(Score score);

    /**
     * Deletes a score from the database.
     * @param score The score to delete.
     */
    void deleteScore(Score score);

    /**
     * Retrieves a score from the database.
     * @param id The id of the score to retrieve.
     * @return The score with the given id, or null if not found.
     */
    Score getScore(int id);

    /**
     * Retrieves all scores from the database.
     * @return A list of all scores in the database.
     */
    List<Score> getAllScores();

    /**
     * Clears all data from the scores table.
     */
    void clearData();

    /**
     * Inserts sample data into the scores table.
     */
    void insertSampleData();
}
