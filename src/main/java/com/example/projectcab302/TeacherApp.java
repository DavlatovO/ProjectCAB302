package com.example.projectcab302.applications;
import com.example.projectcab302.utils.ViewManager;
import com.example.projectcab302.utils.Database;
import com.example.projectcab302.controllers.TranslateController;
import javafx.application.Application;
import javafx.stage.Stage;

public class TeacherApp extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    // View constants are no longer needed with direct method calls

    @Override
    public void start(Stage stage) {
        Database.initializeDatabase();
        // Initialize ViewManager
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
