package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;

public class LandingPageController extends BaseSession{

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
        if (getUser() == null) {
            SceneManager.switchTo("login-view.fxml");
//            SceneManager.switchTo("teacher-view.fxml", user);
        }


    }

    @FXML
    protected void studentDashboard() {
        if (getUser() == null) {
            SceneManager.switchTo("login-view.fxml");
//            SceneManager.switchTo("teacher-view.fxml", user);
        }
//        SceneManager.switchTo("student-view.fxml", user);
    }

}

