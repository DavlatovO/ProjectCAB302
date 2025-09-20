package com.example.projectcab302.Controller;

import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class QuizController {
    @FXML
    protected void saveQuiz() {

    }

    @FXML
    protected void onBack() {
        SceneManager.switchTo("quizzes-view.fxml");
    }

    @FXML
    protected void createQuiz() {
        SceneManager.switchTo("create-quiz.fxml");
    }
    //Note bug where if create quiz is pressed twice the scene manager switches to Teacher-view.fxml

    @FXML
    protected void submitQuiz() {

    }


    public void editQuiz(ActionEvent actionEvent) {
    }

    public void deleteQuiz(ActionEvent actionEvent) {
    }
}
