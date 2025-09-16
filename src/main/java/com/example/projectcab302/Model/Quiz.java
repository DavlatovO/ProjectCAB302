package com.example.projectcab302.Model;

public class Quiz {
    private int quizID;
    private String QuizName;
    private String QuizQuestion;
    private String Answer1;
    private String Answer2;
    private String Answer3;
    private String Answer4;
    private String correctAnswer;
    private String course;

    public Quiz(int quizID, String QuizName, String QuizQuestion, String Answer1, String Answer2, String Answer3, String Answer4, String correctAnswer, String course) {
        this.quizID = quizID;
        this.QuizName = QuizName;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
        this.course = course;
    }

    public Quiz(String QuizName, String QuizQuestion, String Answer1, String Answer2, String Answer3, String Answer4, String correctAnswer, String course) {
        this.QuizName = QuizName;
        this.QuizQuestion = QuizQuestion;
        this.Answer1 = Answer1;
        this.Answer2 = Answer2;
        this.Answer3 = Answer3;
        this.Answer4 = Answer4;
        this.correctAnswer = correctAnswer;
        this.course = course;
    }

    public int getQuizID() {
        return quizID;
    }

    public String getQuizName() {
        return QuizName;
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
    public String getCourse()
    {
        return course;
    }
    public void setQuizName(String QuizName) {
         this.QuizName = QuizName;
    }

    public void setQuizQuestion(String QuizQuestion) {
         this.QuizQuestion = QuizQuestion;
    }

    public void setAnswer1(String Answer1) {
         this.Answer1 = Answer1;
    }
    public void setAnswer2(String Answer2) {
         this.Answer2 = Answer2;
    }
    public void setAnswer3(String Answer3) {
         this.Answer3 = Answer3;
    }
    public void setAnswer4(String Answer4) {
         this.Answer4 = Answer4;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    @Override
    public String toString() {
        return "Quiz{" +
                "quizID=" + quizID +
                ", QuizName='" + QuizName + '\'' +
                ", QuizQuestion='" + QuizQuestion + '\'' +
                ", Answer1=" + Answer1 + '\'' +
                ", Answer2=" + Answer2 + '\'' +
                ", Answer3=" + Answer3 + '\'' +
                ", Answer4=" + Answer4 + '\'' +
                ", correctAnswer=" + correctAnswer + '\'' +
                '}';
    }
}
