package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;

public class LandingPageController {

    @FXML
    protected void login() {
        SceneManager.switchTo("login-view.fxml");
    }

    @FXML
    protected void signUp() {
        SceneManager.switchTo("register-view.fxml");
    }

    @FXML
    protected void teacherDashboard() {
        Teacher user = new Teacher("John", "john@123.com", User.Roles.Teacher, "123");

        SceneManager.switchTo("teacher-view.fxml", user);
    }

    @FXML
    protected void studentDashboard() {
        SceneManager.switchTo("student-view.fxml");
    }

}

