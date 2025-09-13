package com.example.projectcab302.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.example.projectcab302.controllers.TranslateController;

public class ViewManager {
    private static ViewManager instance;
    private Stage primaryStage;

    private ViewManager() {} // Private constructor to prevent instantiation

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private Parent loadView(String fxmlPath) {
        try {
            return javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlPath));
        } catch (Exception e) {
            System.err.println("Error loading view: " + fxmlPath);
            e.printStackTrace();
            return null;
        }
    }
    private void switchView(Parent view) {
        if (primaryStage != null && view != null) {
            Scene currentScene = primaryStage.getScene();
            int width = 800;
            int height = 600;

            Scene newScene = new Scene(view, width, height);

            if (currentScene != null) {
                newScene.getStylesheets().addAll(currentScene.getStylesheets()); // Copy stylesheets from current scene
            }

            primaryStage.setScene(newScene);
        }
    }

    public void switchToTeacherView() { switchView(loadView("/com/example/projectcab302/teacher-view.fxml")); }

    public void switchToSettingsView() {
        switchView(loadView("/com/example/projectcab302/settings-view.fxml"));
    }

    public void switchToQuizzesView() {
        switchView(loadView("/com/example/projectcab302/quizzes-view.fxml"));
    }

    public void switchToTranslateView(String credentialsPath, String projectId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projectcab302/translate-view.fxml"));
            Parent view = loader.load();

            // Get controller and call init
            TranslateController controller = loader.getController();
            controller.init(credentialsPath, projectId);

            switchView(view);
        } catch (Exception e) {
            System.err.println("Error loading Translate View");
            e.printStackTrace();
        }
    }

}
