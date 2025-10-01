package com.example.projectcab302.Controller;

import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class Dashboard extends BaseSession{


    @FXML
    private Label pageHeader;

    @FXML
    private TextField nameInput;

    @FXML
    private GridPane quizzesGrid;

    @FXML
    private Label DashboardLabel;

    @FXML
    Button flashcardsButton;

    @FXML
    private void initialize() throws IOException {
//        if (Objects.equals(this.user.getRole(), "Student")) {
//            DashboardLabel.setText("Student Dashboard");
//        }
//        else {
//            DashboardLabel.setText("Teacher Dashboard");
//        }
    }

    @FXML
    protected void openUserProfile() {
        ViewManager.getInstance().switchToProfileView(
                this.user.getUsername(),
                this.user.getEmail(),
                this.user.getRole()
        );

    }

    @FXML
    protected void openSettings() {
        ViewManager.getInstance().switchToSettingsView();

    }
    @FXML
    protected void onQuizButtonClick() throws IOException {
        SceneManager.switchTo("quiz.fxml",this.user);
    }

    @FXML
    protected void onFlashcardsButtonClick() throws IOException {

        SceneManager.switchTo("courses-view.fxml", this.user);
    }


    @FXML
    protected void logout() {
            SceneManager.switchTo("landingpage.fxml");
        }


}



