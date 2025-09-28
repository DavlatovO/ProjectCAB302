package com.example.projectcab302;

import com.example.projectcab302.Controller.BaseCourseAndSession;
import com.example.projectcab302.Controller.BaseSession;
import com.example.projectcab302.Controller.FlashcardController;
import com.example.projectcab302.Controller.SessionInterface;
import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    public static void switchTo(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource("/com/example/projectcab302/" + fxmlFile));
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For switching scene and injecting user into the next controller
    public static void switchTo(String fxmlFile, User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projectcab302/" + fxmlFile));
            Parent root = fxmlLoader.load();                 // must load before getController()
            BaseSession b = fxmlLoader.getController(); // Polymorphism
            b.setUser(user);
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // For switching scene and injecting course and user into the nex controller
    public static void switchTo(String fxmlFile, User user, Course course) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projectcab302/" + fxmlFile));
            Parent root = fxmlLoader.load();                 // must load before getController()
            BaseCourseAndSession b = fxmlLoader.getController(); // Polymorphism
            b.setUser(user);
            b.setCourse(course);
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
