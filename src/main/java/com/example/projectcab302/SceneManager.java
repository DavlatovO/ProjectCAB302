package com.example.projectcab302;

import com.example.projectcab302.z_Misc.AppSettings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class SceneManager {

    private static Stage stage;

    private static final Deque<Scene> history = new ArrayDeque<>();

    private static final String LIGHT = "/com/example/projectcab302/style.css";
    private static final String DARK  = "/com/example/projectcab302/style-dark.css";

    private static String cssUrl(String resourcePath) {
        var url = SceneManager.class.getResource(resourcePath);
        if (url == null) {
            System.err.println("CSS not found on classpath: " + resourcePath);
            return null;
        }
        return url.toExternalForm();
    }

    public static void setStage(Stage s) {
        stage = s;
        // Minimal addition: allow window-close to navigate back if possible
        if (stage != null) {
            stage.setOnCloseRequest(evt -> {
                if (goBack()) {
                    evt.consume();
                }
            });
        }
    }

    public static void switchTo(String fxmlFile) {
        // Minimal change: support both bare filenames and absolute resource paths
        String resource = fxmlFile.startsWith("/") ? fxmlFile : "/com/example/projectcab302/" + fxmlFile;
        try {
            // Minimal change: push current scene onto a history stack before switching
            if (stage != null && stage.getScene() != null) {
                history.push(stage.getScene());
            }

            Parent root = FXMLLoader.load(SceneManager.class.getResource(resource));
            Scene next = new Scene(root, 1000, 600);

            // ensure exactly one theme stylesheet is present on new scenes
            boolean dark = AppSettings.getInstance().isDarkMode();
            String css = cssUrl(dark ? DARK : LIGHT);
            next.getStylesheets().clear();                // don’t fight with stale entries
            if (css != null) next.getStylesheets().add(css);

            stage.setScene(next);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean goBack() {
        if (!history.isEmpty() && stage != null) {
            Scene previous = history.pop();

            boolean dark = AppSettings.getInstance().isDarkMode();
            String css = cssUrl(dark ? DARK : LIGHT);
            previous.getStylesheets().clear();
            if (css != null) previous.getStylesheets().add(css);

            stage.setScene(previous);
            stage.show();
            return true;
        }
        return false;
    }

    /** Optional: clear navigation history. */
    public static void clearHistory() {
        history.clear();
    }

    /**
     * Convenience: load the profile view and inject simple data.
     * Keeps changes minimal and isolated — uses reflection to avoid hard dependency.
     */
    public static void switchToProfileView(String name, String email, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    SceneManager.class.getResource("/com/example/projectcab302/profile-view.fxml"),
                    "FXML not found: /com/example/projectcab302/profile-view.fxml"
            ));
            Parent root = loader.load();

            Object controller = loader.getController();
            try {
                controller.getClass()
                        .getMethod("setProfileData", String.class, String.class, String.class)
                        .invoke(controller, name, email, role);
            } catch (ReflectiveOperationException reflectionProblem) {
                System.err.println("ProfileController#setProfileData(String,String,String) not found or failed.");
                reflectionProblem.printStackTrace();
            }

            if (stage != null && stage.getScene() != null) {
                history.push(stage.getScene());
            }

            Scene next = new Scene(root, 1000, 600);

            // theme on profile scene
            boolean dark = AppSettings.getInstance().isDarkMode();
            next.getStylesheets().removeAll(LIGHT, DARK);
            next.getStylesheets().add(dark ? DARK : LIGHT);

            stage.setScene(next);
            stage.show();

        } catch (Exception ex) {
            System.err.println("Error loading profile-view.fxml");
            ex.printStackTrace();
        }
    }

    /** Tiny helper controllers can call after toggling theme. */
    public static void refreshThemeOnCurrentScene() {
        if (stage == null) return;
        Scene s = stage.getScene();
        if (s == null) return;
        boolean dark = AppSettings.getInstance().isDarkMode();
        String css = cssUrl(dark ? DARK : LIGHT);
        s.getStylesheets().clear();
        if (css != null) s.getStylesheets().add(css);
    }

}
