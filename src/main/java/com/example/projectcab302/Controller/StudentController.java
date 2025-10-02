package com.example.projectcab302.Controller;

import com.example.projectcab302.Utils.Session;
import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;

public class StudentController {

    @FXML
    protected void openUserProfile() {
        ViewManager.getInstance().switchToProfileView(
                "Student Name",
                "student@example.com",
                "Student"
        );
    }

    @FXML
    protected void openSettings() {
        ViewManager.getInstance().switchToSettingsView();
    }

    @FXML
    protected void logout() {
        Session.clear();
        SceneManager.switchTo("landingpage.fxml");
    }

    @FXML
    protected void quiz() {
    SceneManager.switchTo("quizzes-view.fxml");
    }

    @FXML
    protected void createFlashcard() {
        SceneManager.switchTo("flashcard-view.fxml");
    }

    @FXML
    protected void viewFlashcards() {
        SceneManager.switchTo("createFlashcard-view.fxml");
    }

}
