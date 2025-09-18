package com.example.projectcab302.Controller;

import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.*;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.String;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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