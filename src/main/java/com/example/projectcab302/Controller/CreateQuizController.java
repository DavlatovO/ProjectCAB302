package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.lang.String;


public class CreateQuizController {
    @FXML
    public TextField courseField;
    @FXML
    private TextField questionField;
    @FXML
    private TextField optionAField;
    @FXML
    private TextField optionBField;
    @FXML
    private TextField optionCField;
    @FXML
    private TextField optionDField;
    @FXML
    private TextField answerField;
    @FXML
    private Label errorQuizLabel;

    @FXML
    private IQuizDAO quizDAO;

    @FXML
    protected void submitQuiz(ActionEvent actionEvent) {
        quizDAO = new SqlQuizDAO();
        quizDAO.clearData();

        String question = questionField.getText();
        String optionA = optionAField.getText();
        String optionB = optionBField.getText();
        String optionC = optionCField.getText();
        String optionD = optionDField.getText();
        String answer = answerField.getText();
        String course = courseField.getText();

        if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty()) {
            errorQuizLabel.setText("Please fill in all fields.");
            return;
        }
        Quiz quizs = new Quiz(question, optionA, optionB, optionC, optionD, answer);
        quizDAO.addQuiz(quizs);

    }

    @FXML
    Button backButton;
    @FXML
    protected void onBack() {
        SceneManager.switchTo("teacher-view.fxml");
    }

}