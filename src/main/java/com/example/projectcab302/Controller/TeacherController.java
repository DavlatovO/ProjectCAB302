package com.example.projectcab302.Controller;


import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.*;
import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the teacher dashboard view.

 */
public class TeacherController extends BaseSession{
    @FXML
    private Label pageHeader;

    @FXML
    private TextField nameInput;
    
    @FXML
    private GridPane quizzesGrid;




    @FXML
    Button flashcardsButton;

    @FXML
    protected void openUserProfile() {
        ViewManager.getInstance().switchToProfileView(
                "Teacher Name",
                "teacher@example.com",
                "Teacher"
        );

    }

    @FXML
    protected void openSettings() {
        ViewManager.getInstance().switchToSettingsView();

    }
    @FXML
    protected void onQuizButtonClick() throws IOException {
        SceneManager.switchTo("courses-view.fxml", this.user, "Edit-Quiz.fxml");
    }

    @FXML
    protected void onFlashcardsButtonClick() throws IOException {
        SceneManager.switchTo("courses-view.fxml", this.user, "createFlashcard-view.fxml");
    }

    @Override
    public void afterUserisSet() {
        // Student student = (Student) this.user;
        IScoresDAO scoresDAO = new SqliteScoreDAO();
        List<Score> scores = scoresDAO.getAllScores();
        System.out.println(scores);

        IUserDAO userDAO = new SqliteUserDAO();
        List<Student> students = userDAO.getAllStudents();
        System.out.println(students.get(0).getUsername());



    }

    @FXML
    protected void logout() {
        SceneManager.switchTo("landingpage.fxml");
    }


}

