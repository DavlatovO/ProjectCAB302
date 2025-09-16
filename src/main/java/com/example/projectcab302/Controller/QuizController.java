package com.example.projectcab302.Controller;

import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;


public class QuizController {
    @FXML
    protected void saveQuiz() {

    }


    @FXML
    protected void onBackButtonClick() {
        SceneManager.switchTo("teacher-view.fxml");
    }

    @FXML
    protected void createQuiz() {
        SceneManager.switchTo("create-quiz.fxml");
    }


    @FXML
    protected void submitQuiz() {

    }


}
