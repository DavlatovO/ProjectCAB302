package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.UserData;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;

    @FXML
    protected void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
        String role = roleComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        if (UserData.registerUser(username, password, email, role)) {
            errorLabel.setText("Registered successfully as " + role + "!");
        } else {
            errorLabel.setText("Username already exists.");
        }

        if (!isValidEmail(email)) {
            errorLabel.setText("Invalid email format.");
            return;
        }
    }

    @FXML
    protected void switchToLogin() throws IOException {
        SceneManager.switchTo("login-view.fxml");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}

