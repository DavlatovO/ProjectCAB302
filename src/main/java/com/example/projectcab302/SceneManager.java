package com.example.projectcab302;

import com.example.projectcab302.Controller.*;
import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Utility class for managing scene transitions in the JavaFX application.
 * <p>
 * Provides overloaded methods for switching between scenes while
 * optionally injecting {@link User}, {@link Course}, or FXML path data
 * into controllers that extend shared base controller classes.
 */
public class SceneManager {

    /** The primary stage for the application. */
    private static Stage stage;

    /**
     * Sets the main application stage.
     *
     * @param s the primary stage
     */
    public static void setStage(Stage s) {
        stage = s;
    }

    /**
     * Switches to a new scene using the specified FXML file.
     *
     * @param fxmlFile the name of the FXML file (within /com/example/projectcab302/)
     */
    public static void switchTo(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/com/example/projectcab302/" + fxmlFile));
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to a new scene and injects a {@link User} into the controller.
     * <p>
     * The target controller must extend {@link BaseSession}.
     *
     * @param fxmlFile the FXML file to load
     * @param user     the user to inject into the next controller
     */
    public static void switchTo(String fxmlFile, User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projectcab302/" + fxmlFile));
            Parent root = fxmlLoader.load();
            BaseSession controller = fxmlLoader.getController();
            controller.setUser(user);
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to a new scene and injects both a {@link User} and the next FXML path.
     * <p>
     * The target controller must extend {@link BaseFXMLandSession}.
     *
     * @param fxmlFile the current FXML file to load
     * @param user     the user to inject into the controller
     * @param nextfxml the next FXML file path for navigation
     */
    public static void switchTo(String fxmlFile, User user, String nextfxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projectcab302/" + fxmlFile));
            Parent root = fxmlLoader.load();
            BaseFXMLandSession controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setFXML(nextfxml);
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to a new scene and injects both a {@link User} and {@link Course}.
     * <p>
     * The target controller must extend {@link BaseCourseAndSession}.
     *
     * @param fxmlFile the FXML file to load
     * @param user     the user to inject
     * @param course   the course to inject
     */
    public static void switchTo(String fxmlFile, User user, Course course) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projectcab302/" + fxmlFile));
            Parent root = fxmlLoader.load();
            BaseCourseAndSession controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setCourse(course);
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
