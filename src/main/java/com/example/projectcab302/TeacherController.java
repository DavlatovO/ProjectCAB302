package com.example.projectcab302;

import com.example.projectcab302.utils.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollPane;
import java.util.ArrayList;
import java.util.List;

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

//    protected void onSettingsButtonClick() {
//        ViewManager.getInstance().switchToSettingsView();
//    }

    @FXML
    protected void onBackButtonClick() {
        ViewManager.getInstance().switchToTeacherView();
    }

}
