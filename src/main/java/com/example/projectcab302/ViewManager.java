package com.example.projectcab302;

import com.example.projectcab302.z_Misc.AppSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.Deque;

public class ViewManager {
    private static ViewManager instance;
    private Stage primaryStage;

    private final Deque<Scene> history = new ArrayDeque<>();

    private boolean darkMode = false;

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

    /* ================= THEME API ================= */

    /** Set dark mode on/off and apply to the current scene (and any future ones). */
    public void setDarkMode(boolean enable) {
        this.darkMode = enable;
        if (primaryStage != null) {
            AppSettings.getInstance().applyToScene(primaryStage.getScene());
        }
        // If you have AppSettings and want to persist:
        // AppSettings.getInstance().setDarkMode(enable);
    }

    /** Convenience toggle. */
    public void toggleDarkMode() {
        setDarkMode(!darkMode);
    }

    public boolean isDarkMode() {
        return AppSettings.getInstance().isDarkMode();
    }

    /** Ensure stylesheet is present and the root has correct style classes. */
    private void applyTheme(Scene scene) {
        AppSettings.getInstance().applyToScene(scene);
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

            double sceneW = (primaryStage.getWidth()  > 0) ? primaryStage.getWidth()  :
                    (currentScene != null && currentScene.getWidth()  > 0) ? currentScene.getWidth()  : 900;
            double sceneH = (primaryStage.getHeight() > 0) ? primaryStage.getHeight() :
                    (currentScene != null && currentScene.getHeight() > 0) ? currentScene.getHeight() : 600;

            Scene newScene = new Scene(view, sceneW, sceneH);

            if (currentScene != null) {
                newScene.getStylesheets().addAll(currentScene.getStylesheets()); // Copy stylesheets from current scene
                history.push(currentScene);
            }

            applyTheme(newScene);

            primaryStage.setScene(newScene);
            primaryStage.setWidth(sceneW);
            primaryStage.setHeight(sceneH);
            primaryStage.requestFocus();
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
        switchView(loadView("/com/example/projectcab302/translate-view.fxml"));}

    public boolean goBack() {
        if (primaryStage == null || history.isEmpty()) return false;

        double w = primaryStage.getWidth();
        double h = primaryStage.getHeight();

        Scene prev = history.pop();

        applyTheme(prev);

        primaryStage.setScene(prev);

        primaryStage.setWidth(w);
        primaryStage.setHeight(h);

        primaryStage.requestFocus();
        return true;
    }

    public void switchToProfileView(String name, String email, String role) {
        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("/com/example/projectcab302/profile-view.fxml"));
            javafx.scene.Parent view = loader.load();
            com.example.projectcab302.Controller.ProfileController controller = loader.getController();
            controller.setProfileData(name, email, role);
            switchView(view);
        } catch (Exception ex) {
            System.err.println("Error loading profile-view.fxml");
            ex.printStackTrace();
        }
    }
}
