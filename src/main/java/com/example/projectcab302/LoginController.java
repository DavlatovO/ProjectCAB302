package com.example.projectcab302;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        String role = UserData.validateLoginAndGetRole(username, password);

        if (role != null) {
            errorLabel.setText("Login successful as " + role + "!");
            // TODO: Navigate to different dashboard if role = "Teacher"
            try {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                FXMLLoader fxmlLoader;

                if (role.equalsIgnoreCase("Teacher")) {
                    fxmlLoader = new FXMLLoader(getClass().getResource("teacher-dashboard.fxml"));
                } else {
                    fxmlLoader = new FXMLLoader(getClass().getResource("student-dashboard.fxml"));
                }
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            }

            catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Failed to load dashboard for " + role + ".");
            }

        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    protected void switchToRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.setScene(scene);
    }
}
