package com.example.projectcab302;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    public Button TranslateDemo;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField nameInput;



    @FXML
    protected void onHelloButtonClick() {
        welcomeLabel.setText("Hello, " + nameInput.getText() + "!");
    }



    @FXML
    protected void TranslateDemoTransition() throws IOException {
        SceneManager.switchTo("translate-view.fxml");
    }
}
