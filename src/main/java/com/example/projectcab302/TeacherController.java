package com.example.projectcab302;

import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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
    protected void onBackButtonClick() {
        ViewManager.getInstance().switchToTeacherView();
    }

}
