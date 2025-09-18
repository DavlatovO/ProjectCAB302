package com.example.projectcab302.Controller;


import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class TeacherController {
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
        SceneManager.switchTo("quiz.fxml");
    }

    @FXML
    protected void onFlashcardsButtonClick() throws IOException {
        SceneManager.switchTo("courses-view.fxml");
    }


    @FXML
    protected void logout() {
        SceneManager.switchTo("landingpage.fxml");
    }
}

