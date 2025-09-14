package com.example.projectcab302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 1000;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        // scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }
}

//Need to add more features
