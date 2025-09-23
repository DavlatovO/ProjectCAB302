package com.example.projectcab302.Model;

public class Quiz {
    private int quizID;
    private String QuizQuestion;
    private String Answer1;
    private String Answer2;
    private String Answer3;
    private String Answer4;
    private String correctAnswer;
    private String Course;

    public Quiz(int quizID,String course, String QuizQuestion, String Answer1, String Answer2, String Answer3, String Answer4, String correctAnswer) {
        this.quizID = quizID;
        this.Course = course;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
    }

    public Quiz(String Course, String QuizQuestion, String Answer1, String Answer2, String Answer3, String Answer4, String correctAnswer) {
        this.Course = Course;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
    }

//    public Quiz(String QuizQuestion, String Answer1, String Answer2, String Answer3, String Answer4, String correctAnswer) {
//        this.QuizQuestion = QuizQuestion;
//        this.Answer1 = Answer1;
//        this.Answer2 = Answer2;
//        this.Answer3 = Answer3;
//        this.Answer4 = Answer4;
//        this.correctAnswer = correctAnswer;
//    }

    public int getQuizID() {
        return quizID;
    }
    public String getCourse()
    {
        return Course;
    }
    public String getQuizQuestion() {
        return QuizQuestion;
    }
    public String getAnswer1() {
        return Answer1;
    }
    public String getAnswer2() {
        return Answer2;
    }
    public String getAnswer3() {
        return Answer3;
    }
    public String getAnswer4() {
        return Answer4;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }


    public void setQuizID(Integer quizID) {
        if (quizID == null) throw new IllegalArgumentException("quizID cannot be null");
        this.quizID = quizID;
    }
    public void setQuizQuestion(String QuizQuestion) {
        if (QuizQuestion == null) throw new IllegalArgumentException("QuizQuestion cannot be null");
        this.QuizQuestion = QuizQuestion;
    }
    public void setAnswer1(String Answer1) {
        if (Answer1 == null) throw new IllegalArgumentException("Answer1 cannot be null");
        this.Answer1 = Answer1;
    }

    public void setAnswer2(String Answer2) {
        if (Answer2 == null) throw new IllegalArgumentException("Answer2 cannot be null");
        this.Answer2 = Answer2;
    }

    public void setAnswer3(String Answer3) {
        if (Answer3 == null) throw new IllegalArgumentException("Answer3 cannot be null");
        this.Answer3 = Answer3;
    }

    public void setAnswer4(String Answer4) {
        if (Answer4 == null) throw new IllegalArgumentException("Answer4 cannot be null");
        this.Answer4 = Answer4;
    }

    public void setCorrectAnswer(String correctAnswer) {
        if (correctAnswer == null) throw new IllegalArgumentException("CorrectAnswer cannot be null");
        this.correctAnswer = correctAnswer;
    }
    public void setCourse(String course) {
        this.Course = course;
    }


    @Override
    public String toString() {
        return "Quiz{" +
                "quizID=" + quizID +
                ", QuizQuestion='" + QuizQuestion + '\'' +
                ", Answer1=" + Answer1 + '\'' +
                ", Answer2=" + Answer2 + '\'' +
                ", Answer3=" + Answer3 + '\'' +
                ", Answer4=" + Answer4 + '\'' +
                ", correctAnswer=" + correctAnswer + '\'' +
                '}';
    }
}
