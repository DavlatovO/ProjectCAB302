package com.example.projectcab302.Model;

import com.example.projectcab302.Persistence.*;
import com.example.projectcab302.modelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a course in the flashcard application.
 * A course has a unique ID, a title, and is associated with multiple flashcards.
 */
public class Course {

    /**
     * Unique identifier for the course (matches the database ID).
     */
    private int id;

    /**
     * The title of the course (e.g., "CAB202").
     */
    private String title;

    /**
     * Data Access Object (DAO) for flashcards.
     * Used to fetch flashcards from the database.
     */
    private IFlashcardDAO flashcardDAO = new SqliteFlashcardDAO();

    private IQuizDAO quizDAO = new SqlQuizDAO();

    /**
     * Constructs a new Course instance with the specified title.
     *
     * @param title the title of the course
     * @throws IllegalArgumentException if the title is invalid
     */
    public Course(String title) {
        this.setTitle(title);
    }

    /**
     * Retrieves the list of flashcards that belong specifically to this course.
     * It filters the cached flashcards based on the course title.
     *
     * @return a list of flashcards associated with this course
     */
    public List<Flashcard> getFlashcards() {
        /**
         * Cached list of all flashcards retrieved from the database.
         */
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
        List<Flashcard> courseCards = new ArrayList<>();
        for (Flashcard card : flashcards) {
            if (Objects.equals(card.getCourse(), this.title)) {
                courseCards.add(card);
            }
        }
        return courseCards;
    }

    public List<Quiz> getQuiz() {
        /**
         * Cached list of all flashcards retrieved from the database.
         */
        List<Quiz> quizzes = quizDAO.getAllQuizs();
        List<Quiz> courseQuizzes = new ArrayList<>();
        System.out.println(quizzes);
        for (Quiz quiz : quizzes) {
            if (Objects.equals(quiz.getCourse(), this.title)) {
                courseQuizzes.add(quiz);
            }
        }
        System.out.println(courseQuizzes);
        return courseQuizzes;
    }

    /**
     * Gets the unique ID of the course.
     *
     * @return the ID of the course
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID for the course.
     *
     * @param id the ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title of the course.
     *
     * @return the course title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the course after validating and trimming the input.
     *
     * @param title the title to set
     * @throws IllegalArgumentException if the title is invalid
     */
    public void setTitle(String title) {
        this.title = modelUtils.checkValidityAndTrim(title, "Course Title");
    }
}
