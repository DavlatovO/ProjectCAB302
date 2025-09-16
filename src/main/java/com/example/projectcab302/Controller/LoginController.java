package com.example.projectcab302.Controller;

import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.SqliteFlashcardDAO;
import com.example.projectcab302.Model.UserData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void initialize() {

        UserData user = new UserData();

    }

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


    // quick login as teacher for testing purposes
    @FXML
    Button loginTeacher;
    @FXML
    protected void onLoginAsTeacher() throws IOException {
        Stage stage = (Stage) loginTeacher.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    Button loginStudent;
    @FXML
    protected void onLoginAsStudent() throws IOException {
        Stage stage = (Stage) loginTeacher.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

}
