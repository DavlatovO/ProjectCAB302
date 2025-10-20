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

public class TeacherController extends BaseSession{
    @FXML
    private Label pageHeader;

    @FXML
    private TextField nameInput;
    
    @FXML
    private GridPane quizzesGrid;



//    @FXML
//    protected void onTestingButtonClick() {
//        ViewManager.getInstance().switchToTranslateView("C:/Users/anodi/Downloads/trans-472009-db2d770946a8.json","trans-472009");
//    }

//    protected void onSettingsButtonClick() {
//        ViewManager.getInstance().switchToSettingsView();
//    }

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

        ISubmissionDAO submissionDAO = new SqliteSubmissionDAO();
        List<Submission> submissions = submissionDAO.getAllSubmissions();
        System.out.println(submissions);
    }

    @FXML
    protected void logout() {
        SceneManager.switchTo("landingpage.fxml");
    }


}

