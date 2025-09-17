package com.example.projectcab302.Controller;

import com.example.projectcab302.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

// NOTE: This controller does not load FXMLs directly anymore.
// It delegates to ViewManager just like TeacherController.

public class StudentController {
    @FXML private BorderPane root;
    @FXML private Button settingsButton, quizButton, createFlashcardButton, viewFlashcardsButton, logoutButton;
    @FXML private Label pageHeader;

    @FXML
    private void initialize() {
        if (pageHeader != null) pageHeader.setText("Welcome to the Dashboard");
    }

    @FXML
    protected void handleOpenUserProfile() {
        // TODO: replace with real logged-in user info
        ViewManager.getInstance().switchToProfileView(
                "Student Name",
                "student@example.com",
                "Student"
        );
    }

    @FXML
    protected void handleOpenSettings() {
        ViewManager.getInstance().switchToSettingsView();
    }

    @FXML protected void handleQuiz() {
        System.out.println("[Student] Quizzes");
    }
    @FXML protected void handleCreateFlashcard() {
        System.out.println("[Student] Create Flashcard");
    }
    @FXML protected void handleViewFlashcards() {
        System.out.println("[Student] View Flashcards");
    }
    @FXML protected void handleLogout() {
        System.out.println("[Student] Logout");
    }

    @FXML protected void handleBack() {
        ViewManager.getInstance().goBack();
    }

}
