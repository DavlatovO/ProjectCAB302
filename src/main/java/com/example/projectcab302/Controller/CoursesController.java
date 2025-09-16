package com.example.projectcab302.Controller;


import com.example.projectcab302.Model.*;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CoursesController {

    @FXML
    private HBox courseBox;

    private IFlashcardDAO flashcardDAO;

    private ICoursesDAO courseDAO;
    private List<Course> courses;

    private Course course;

    @FXML
    private void initialize() {
        flashcardDAO = new SqliteFlashcardDAO();
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();


        for (int i = 0; i < Math.min(courses.size(), 3); i++) {
            final int idx = i;                           // <- final copy
            Button btn = new Button(courses.get(idx).getTitle());

            btn.setOnAction(e -> {
                try {
                    course = courses.get(idx);
                    System.out.println(courses.get(idx).getTitle());
                    onCourse();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            btn.setPrefWidth(200);
            courseBox.getChildren().add(btn);
        }

    }

    private void onCourse() throws IOException {
        SceneManager.switchTo("createFlashcard-view.fxml");
    }

    @FXML Button createCourse;

    @FXML
    private void onCreateCourse() throws IOException {
       SceneManager.switchTo("flashcard.view.fxml");
    }

    @FXML Button back;
    @FXML
    private void onBack() throws IOException {
       SceneManager.switchTo("teacher-view.fxml");
    }

}
