package com.example.projectcab302.Controller;

import com.example.projectcab302.Utils.Session;
import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;

public class StudentController {

    @FXML
    protected void openUserProfile() {
        SceneManager.switchToProfileView(
                "Student Name",
                "student@example.com",
                "Student"
        );
    }

    @FXML
    protected void openSettings() {
        SceneManager.switchTo("settings-view.fxml");
    }

    @FXML
    protected void logout() {
        Session.clear();
        SceneManager.switchTo("landingpage.fxml");
    }

    @FXML
    protected void quiz() {

    }

    @FXML
    protected void createFlashcard() {

    }

    @FXML
    protected void viewFlashcards() {

    }

}
