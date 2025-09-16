package com.example.projectcab302.Model;

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
     * @param question the question text (e.g., "What is 2 + 2?")
     * @param answer   the answer text (e.g., "4")
     *                 <p><b>Note:</b> This constructor does not validate or trim inputs.
     *                 Consider validating upstream (non-null, non-blank) if required by your UI/DB.</p>
     * @param course
     */
    public Flashcard(String course, String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.course = course;
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
        this.question = question;
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
        this.answer = answer;
    }

    public String getCourse() {
        return this.course;
    }


    public void setCourse(String Course) {
        this.course = Course;
    }

    // @Override public String toString() { ... }      // helpful for logging/debugging
    // @Override public boolean equals(Object o) { ... } and @Override public int hashCode() { ... }
    // Consider making fields @NotNull and trimming inputs in setters .
}
