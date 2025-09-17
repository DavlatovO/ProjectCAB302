package com.example.projectcab302;

import com.example.projectcab302.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewManager.getInstance().setPrimaryStage(primaryStage);   // <-- add this
        SceneManager.setStage(primaryStage);
        SceneManager.switchTo("landingpage.fxml"); //Program starts at landing page as soon as it runs
    }

    public static void main(String[] args) {
        launch(args);
    }
}
