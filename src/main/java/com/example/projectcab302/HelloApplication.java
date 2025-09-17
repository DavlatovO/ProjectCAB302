package com.example.projectcab302;

import com.example.projectcab302.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setStage(primaryStage);
        SceneManager.switchTo("landingpage.fxml"); //Program starts at landing page as soon as it runs
    }

    public static void main(String[] args) {
        launch(args);
    }
}
