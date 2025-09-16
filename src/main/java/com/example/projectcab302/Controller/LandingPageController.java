package com.example.projectcab302.Controller;

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
        SceneManager.switchTo("teacher-view.fxml");
    }

    @FXML
    protected void studentDashboard() {
        SceneManager.switchTo("student-view.fxml");
    }

}

