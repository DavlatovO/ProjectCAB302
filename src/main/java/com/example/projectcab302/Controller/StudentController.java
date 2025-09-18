package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Session;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;

public class StudentController {

    @FXML
    protected void openUserProfile() {

    }

    @FXML
    protected void openSettings() {

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
