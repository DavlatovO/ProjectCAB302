package com.example.projectcab302;
import com.example.projectcab302.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class TeacherApp extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    // View constants are no longer needed with direct method calls

    @Override
    public void start(Stage stage) {
        ViewManager viewManager = ViewManager.getInstance();
        viewManager.setPrimaryStage(stage);

        stage.setTitle("Teacher Dashboard");
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);

        viewManager.switchToTeacherView();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
