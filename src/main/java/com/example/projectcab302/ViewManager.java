package com.example.projectcab302;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.util.Objects;

import java.util.ArrayDeque;
import java.util.Deque;

public class ViewManager {
    private static ViewManager instance;
    private Stage primaryStage;

    private final Deque<Scene> history = new ArrayDeque<>();

    private ViewManager() {} // Private constructor to prevent instantiation

    public static ViewManager getInstance() {
        if (instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setOnCloseRequest(null); // or do nothing at all
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

            // make sure every root can use .app-root and .dark selectors
            if (!view.getStyleClass().contains("app-root")) {
                view.getStyleClass().add("app-root");
            }

            Scene newScene = new Scene(view, width, height);

            if (currentScene != null) {
                history.push(currentScene);
            }

            newScene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), this::goBack);

            primaryStage.setScene(newScene);

            AppSettings.getInstance().applyToScene(newScene);

            primaryStage.requestFocus();
        }
    }

    // NEW: Go back to the previous scene if available
    public boolean goBack() {
        if (primaryStage == null || history.isEmpty()) return false;
        Scene prev = history.pop();
        primaryStage.setScene(prev);
        AppSettings.getInstance().applyToScene(prev);
        primaryStage.requestFocus();
        return true;
    }

    // Optional: clear history (useful on hard logout)
    public void clearHistory() {
        history.clear();
    }

    public void switchToProfileView(String name, String email, String role) {
        try {
            // Load profile-view.fxml with a fresh FXMLLoader so we can access its controller
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/example/projectcab302/profile-view.fxml")
            );
            javafx.scene.Parent root = loader.load();

            // Set profile data on the controller (if available)
            com.example.projectcab302.Controller.ProfileController pc = loader.getController();
            if (pc != null) {
                pc.setProfileData(name, email, role);
            }

            // Reuse your existing scene switch helper
            switchView(root);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open Profile view", e);
        }
    }

    public void switchToTeacherView() {
        switchView(loadView("/com/example/projectcab302/teacher-view.fxml"));
    }

    public void switchToStudentView() {
        switchView(loadView("/com/example/projectcab302/student-view.fxml"));
    }

    public void switchToSettingsView() {

        switchView(loadView("/com/example/projectcab302/settings-view.fxml"));
    }

    public void switchToQuizzesView() {

        switchView(loadView("/com/example/projectcab302/quizzes-view.fxml"));
    }

    public void switchToTranslateView(String credentialsPath, String projectId) {
        switchView(loadView("/com/example/projectcab302/translate-view.fxml"));
    }

}
