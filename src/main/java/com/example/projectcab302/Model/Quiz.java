package com.example.projectcab302.Model;

/**
 * Represents a quiz question belonging to a specific course.
 * <p>
 * Each quiz contains a question, four answer options, one correct answer,
 * and performance statistics (number of correct and wrong responses).
 * </p>
 */
public class Quiz {

    /** Unique identifier for the quiz entry. */
    private int quizID;

    /** The text of the quiz question. */
    private String QuizQuestion;

    /** Option 1 for the quiz question. */
    private String Answer1;

    /** Option 2 for the quiz question. */
    private String Answer2;

    /** Option 3 for the quiz question. */
    private String Answer3;

    /** Option 4 for the quiz question. */
    private String Answer4;

    /** The correct answer text. */
    private String correctAnswer;

    /** The course to which this quiz belongs. */
    private String Course;

    /** The number of correct responses to this quiz. */
    private int correct;

    /** The number of incorrect responses to this quiz. */
    private int wrong;

    // ─────────────────────────────
    // Constructors
    // ─────────────────────────────

    /**
     * Constructs a new {@code Quiz} object with full parameters including statistics.
     *
     * @param Course         the course code or name
     * @param QuizQuestion   the text of the quiz question
     * @param Answer1        the first answer option
     * @param Answer2        the second answer option
     * @param Answer3        the third answer option
     * @param Answer4        the fourth answer option
     * @param correctAnswer  the correct answer text
     * @param correct        the number of times answered correctly
     * @param wrong          the number of times answered incorrectly
     */
    public Quiz(String Course, String QuizQuestion, String Answer1, String Answer2, String Answer3,
                String Answer4, String correctAnswer, int correct, int wrong) {
        this.Course = Course;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
        this.wrong = wrong;
    }

    /**
     * Constructs a new {@code Quiz} object without tracking statistics.
     *
     * @param Course         the course code or name
     * @param QuizQuestion   the text of the quiz question
     * @param Answer1        the first answer option
     * @param Answer2        the second answer option
     * @param Answer3        the third answer option
     * @param Answer4        the fourth answer option
     * @param correctAnswer  the correct answer text
     */
    public Quiz(String Course, String QuizQuestion, String Answer1, String Answer2,
                String Answer3, String Answer4, String correctAnswer) {
        this.Course = Course;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
    }

    // ─────────────────────────────
    // Getters
    // ─────────────────────────────

    /**
     * Returns the quiz ID.
     *
     * @return the quiz ID
     */
    public int getQuizID() {
        return quizID;
    }

    /**
     * Returns the course associated with this quiz.
     *
     * @return the course name or code
     */
    public String getCourse() {
        return Course;
    }

    /**
     * Returns the quiz question text.
     *
     * @return the quiz question
     */
    public String getQuizQuestion() {
        return QuizQuestion;
    }

    /**
     * Returns the first answer option.
     *
     * @return the first answer text
     */
    public String getAnswer1() {
        return Answer1;
    }

    /**
     * Returns the second answer option.
     *
     * @return the second answer text
     */
    public String getAnswer2() {
        return Answer2;
    }

    /**
     * Returns the third answer option.
     *
     * @return the third answer text
     */
    public String getAnswer3() {
        return Answer3;
    }

    /**
     * Returns the fourth answer option.
     *
     * @return the fourth answer text
     */
    public String getAnswer4() {
        return Answer4;
    }

    /**
     * Returns the correct answer text.
     *
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Returns the number of correct attempts.
     *
     * @return the correct count
     */
    public int getCorrect() {
        return correct;
    }

    /**
     * Returns the number of wrong attempts.
     *
     * @return the wrong count
     */
    public int getWrong() {
        return wrong;
    }

    /**
     * Calculates the percentage of correct answers.
     *
     * @return the average correctness as a percentage (0–100)
     */
    public double getAverage() {
        int total = this.correct + this.wrong;
        if (total == 0) return 0.0;
        return ((double) this.correct / total) * 100.0;
    }

    // ─────────────────────────────
    // Setters
    // ─────────────────────────────

    /**
     * Sets the quiz ID.
     *
     * @param quizID the quiz ID (must not be {@code null})
     * @throws IllegalArgumentException if {@code quizID} is {@code null}
     */
    public void setQuizID(Integer quizID) {
        if (quizID == null)
            throw new IllegalArgumentException("quizID cannot be null");
        this.quizID = quizID;
    }

    /**
     * Sets the quiz question.
     *
     * @param QuizQuestion the question text
     * @throws IllegalArgumentException if {@code QuizQuestion} is {@code null}
     */
    public void setQuizQuestion(String QuizQuestion) {
        if (QuizQuestion == null)
            throw new IllegalArgumentException("QuizQuestion cannot be null");
        this.QuizQuestion = QuizQuestion;
    }

    /** Sets answer option 1. */
    public void setAnswer1(String Answer1) {
        if (Answer1 == null)
            throw new IllegalArgumentException("Answer1 cannot be null");
        this.Answer1 = Answer1;
    }

    /** Sets answer option 2. */
    public void setAnswer2(String Answer2) {
        if (Answer2 == null)
            throw new IllegalArgumentException("Answer2 cannot be null");
        this.Answer2 = Answer2;
    }

    /** Sets answer option 3. */
    public void setAnswer3(String Answer3) {
        if (Answer3 == null)
            throw new IllegalArgumentException("Answer3 cannot be null");
        this.Answer3 = Answer3;
    }

    /** Sets answer option 4. */
    public void setAnswer4(String Answer4) {
        if (Answer4 == null)
            throw new IllegalArgumentException("Answer4 cannot be null");
        this.Answer4 = Answer4;
    }

    /**
     * Sets the correct answer.
     *
     * @param correctAnswer the correct answer text
     * @throws IllegalArgumentException if {@code correctAnswer} is {@code null}
     */
    public void setCorrectAnswer(String correctAnswer) {
        if (correctAnswer == null)
            throw new IllegalArgumentException("CorrectAnswer cannot be null");
        this.correctAnswer = correctAnswer;
    }

    /** Sets the associated course. */
    public void setCourse(String course) {
        this.Course = course;
    }

    /** Sets the number of correct attempts. */
    public void setCorrect(int correct) {
        this.correct = correct;
    }

    /** Sets the number of wrong attempts. */
    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    // ─────────────────────────────
    // Object Overrides
    // ─────────────────────────────

    /**
     * Returns a string representation of the quiz, including question and answer data.
     *
     * @return a formatted string describing the quiz
     */
    @Override
    public String toString() {
        return "Quiz{" +
                "quizID=" + quizID +
                ", QuizQuestion='" + QuizQuestion + '\'' +
                ", Answer1='" + Answer1 + '\'' +
                ", Answer2='" + Answer2 + '\'' +
                ", Answer3='" + Answer3 + '\'' +
                ", Answer4='" + Answer4 + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
