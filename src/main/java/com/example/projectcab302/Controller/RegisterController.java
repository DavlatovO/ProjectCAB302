package com.example.projectcab302.Controller;

import com.example.projectcab302.Persistence.DatabaseController;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;


import java.io.IOException;


public class RegisterController {


    public TextField usernameField;
    public TextField emailField;
    public PasswordField passwordField;
    public PasswordField confirmField;
    public ComboBox roleComboBox;
    public Label errorLabel;

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll(User.Roles.values()); // fills with Student, Teacher
    }

    @FXML
    protected void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmField.getText();
        User.Roles role = (User.Roles) roleComboBox.getValue();


        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || role == null) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }
        if (!isValidEmail(email)) {
            errorLabel.setText("Invalid email format.");
            return;
        }
        if (DatabaseController.emailExists(email)) {
            errorLabel.setText("Email already registered!");
        }
        DatabaseController.initDB();
        if (role == User.Roles.Student) {
            Student s1 = new Student(username, email, role, password);
            DatabaseController.saveUser(s1);
            SceneManager.switchTo("student-view.fxml");
        }
        if (role == User.Roles.Teacher) {
            Teacher t1 = new Teacher(username, email, role, password);
            DatabaseController.saveUser(t1);
            SceneManager.switchTo("teacher-view.fxml");
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
