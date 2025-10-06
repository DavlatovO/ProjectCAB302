package com.example.projectcab302.Controller;

import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;

public class StudentController extends BaseSession {

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
        BaseSession.clear();
        SceneManager.switchTo("landingpage.fxml");
    }

    @FXML
    protected void quiz() {
        SceneManager.switchTo("quiz.fxml", this.user);
    }

    @FXML
    protected void createFlashcard() {
        SceneManager.switchTo("createFlashcard-view.fxml");
    }

    @FXML
    protected void viewFlashcards() {
        SceneManager.switchTo("flashcard-view.fxml");
    }

}
