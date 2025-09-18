package com.example.projectcab302.Controller;


import com.example.projectcab302.Model.Session;
import com.example.projectcab302.Model.SqliteFlashcardDAO;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.Model.UserData;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    protected void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        User user = DatabaseController.login(username, password);
        if (user != null) {
            Session.setUser(user);
            switch (user.getRoles()) {
                case Student -> SceneManager.switchTo("student-view.fxml");
                case Teacher -> SceneManager.switchTo("teacher-view.fxml");
            }


        } else {
            errorLabel.setText("Login failed. Try again.");
        }

    }

    //Switching to the Register window from login window
    @FXML
    protected void switchToRegister() throws IOException {
        SceneManager.switchTo("register-view.fxml");
    }


    // quick login as teacher for testing purposes
    @FXML
    Button loginTeacher;
    @FXML
    protected void onLoginAsTeacher() throws IOException {
        SceneManager.switchTo("teacher-view.fxml");
    }

    @FXML
    Button loginStudent;
    @FXML
    protected void onLoginAsStudent() throws IOException {
        SceneManager.switchTo("teacher-view.fxml");
    }

}
