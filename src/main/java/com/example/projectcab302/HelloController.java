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
        Stage stage = (Stage) TranslateDemo.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("translate-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    public void openLoginWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("User Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // This will show the error in your console
        }
    }
}
