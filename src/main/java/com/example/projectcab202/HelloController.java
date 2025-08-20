package com.example.projectcab202;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField favFood;

    @FXML
    private Label foodResponse;

    @FXML
    protected void onNewButtonClick() {
        foodResponse.setText(favFood.getText() + " is very yummy!");
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeLabel.setText("Hello, " + nameInput.getText() + "!");
    }


}
