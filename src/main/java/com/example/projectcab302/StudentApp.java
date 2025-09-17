package com.example.projectcab302;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectcab302/student-view.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Student Dashboard");
        stage.setScene(scene);
        ViewManager viewManager = ViewManager.getInstance();
        viewManager.setPrimaryStage(stage);
        viewManager.switchToStudentView();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
