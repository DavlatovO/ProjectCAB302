package com.example.projectcab302.Controller;

import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherController {
    @FXML
    private Label pageHeader;

    @FXML
    private TextField nameInput;
    
    @FXML
    private GridPane quizzesGrid;

    @FXML
    protected void onQuizButtonClick() {
        ViewManager.getInstance().switchToQuizzesView();
    }

    @FXML
    protected void onTestingButtonClick() {
        ViewManager.getInstance().switchToTranslateView("C:/Users/anodi/Downloads/trans-472009-db2d770946a8.json","trans-472009");
    }

//    protected void onSettingsButtonClick() {
//        ViewManager.getInstance().switchToSettingsView();
//    }

    @FXML
    Button flashcardsButton;

    @FXML
    protected void onFlashcardsButtonClick() throws IOException {
        Stage stage = (Stage) flashcardsButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("courses-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    protected void onBackButtonClick() {
        ViewManager.getInstance().switchToTeacherView();
    }

}
