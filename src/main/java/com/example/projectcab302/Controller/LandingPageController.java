package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import com.example.projectcab302.Persistence.IScoresDAO;
import com.example.projectcab302.Persistence.IUserDAO;
import com.example.projectcab302.Persistence.SqliteScoreDAO;
import com.example.projectcab302.Persistence.SqliteUserDAO;
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
        //Teacher user = new Teacher("John", "john@123.com", User.Roles.Teacher, "123");
        if (user != null){
            SceneManager.switchTo("teacher-view.fxml", user);
        } else {
            SceneManager.switchTo("login-view.fxml");
        }

    }

    @FXML
    protected void studentDashboard() {
        //Student user = new Student("Student", "john@123.com", User.Roles.Student, "123");
        if (user != null){
            SceneManager.switchTo("student-view.fxml", user);
        } else {
            SceneManager.switchTo("login-view.fxml");
        }
    }

}

