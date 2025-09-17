package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateQuizController {
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
    private IQuizDAO quizDAO;

    @FXML
    protected void onsubmitQuiz() throws IOException {
        quizDAO = new SqlQuizDAO();
        quizDAO.clearData();

        if (questionField.isEmpty() || optionAField.isEmpty() || optionBField.isEmpty() || optionCField.isEmpty() || optionDField.isEmpty() || answerField.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        String question = questionField.getText();
        String optionA = optionAField.getText();
        String optionB = optionBField.getText();
        String optionC = optionCField.getText();
        String optionD = optionDField.getText();
        String answer = answerField.getText();
        String course = "CAB302";

        Quiz quizs = new Quiz(question, optionA, optionB, optionC, optionD, answer, course);
        quizDAO.addQuiz(quizs);


    }
}
