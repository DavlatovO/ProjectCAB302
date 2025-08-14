package com.example.projectcab202;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField nameInput;

    @FXML
    protected void onHelloButtonClick() {
        welcomeLabel.setText("Hello, " + nameInput.getText() + "!");
    }
}
