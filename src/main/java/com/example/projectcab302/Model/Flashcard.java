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

    /** The prompt or question shown to the learner. */
    private String question;

    /** The expected answer to the question. */
    private String answer;

    /** The expected answer to the question. */
    private String course;

    /**
     * Constructs a new {@code Flashcard}.
     *
     * @param course the course this flashcard belongs to
     * @param question the question text (e.g., "What is 2 + 2?")
     * @param answer   the answer text (e.g., "4")
     */
    public Flashcard(String course, String question, String answer) {
        setQuestion(question);
        setAnswer(answer);
        setCourse(course);
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
    public String getCourse() {
        return this.course;
    }

    /**
     * Updates the answer text.
     *
     * @param Course the new answer text
     */
    public void setCourse(String Course) {

        this.course = modelUtils.checkValidityAndTrim(Course, "Course title");
    }



}