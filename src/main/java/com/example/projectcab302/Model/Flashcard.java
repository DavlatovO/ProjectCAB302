package com.example.projectcab302.Model;

import com.example.projectcab302.modelUtils;

/**
 * Represents a single flashcard with an identifier, a question, and an answer.
 * <p>

 *
 * <h3>Example</h3>
 * <pre>{@code
 * Flashcard fc = new Flashcard("What is the capital of France?", "Paris");
 * // Later, after inserting into the database:
 * fc.setId(generatedId);
 * }</pre>
 */
public class Flashcard {

    /**
     * Database-generated identifier for this flashcard.
     */
    private int id;

    /**
     * User's identifier for this flashcard.
     */
    private User user;

    /** The prompt or question shown to the learner. */
    private String question;

    /** The expected answer to the question. */
    private String answer;

    /** The expected answer to the question. */
    private Course course;

    /**
     * Constructs a new {@code Flashcard}.
     *
     * @param course the course this flashcard belongs to
     * @param question the question text (e.g., "What is 2 + 2?")
     * @param answer   the answer text (e.g., "4")
     */
    public Flashcard(User user, Course course, String question, String answer) {
        this.user = user;
        this.course = course;
        setQuestion(question);
        setAnswer(answer);
    }

    /**
     * Returns the unique identifier for this flashcard.
     *
     * @return the flashcard id; often assigned by the database after insert
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the unique identifier for this flashcard.
     *
     * @return the user who created the course
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the unique identifier for this flashcard.
     * <p>Typically called by DAO code after the row is inserted and an id is generated.</p>
     *
     * @param id the database id to associate with this flashcard
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the question text.
     *
     * @return the question
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Updates the question text.
     *
     * @param question the new question text
     */
    public void setQuestion(String question) {

        this.question = modelUtils.checkValidityAndTrim(question, "Flashcard question");
    }

    /**
     * Returns the answer text.
     *
     * @return the answer
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * Updates the answer text.
     *
     * @param answer the new answer text
     */
    public void setAnswer(String answer) {

        this.answer = modelUtils.checkValidityAndTrim(answer, "Flashcard answer");
    }

    /**
     * Returns the course the flashcard belongs to.
     *
     * @return the course
     */
    public Course getCourse() {
        return this.course;
    }

    /**
     * Updates the answer text.
     *
     * @param course the new answer text
     */
    public void setCourse(Course course) {
        this.course = course;
    }



}