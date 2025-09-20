package com.example.projectcab302.Controller;

import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CoursesController {

    @FXML
    private HBox courseBox;

    @FXML
    private TextField courseField;

    private IFlashcardDAO flashcardDAO;

    private ICoursesDAO courseDAO;
    private List<Course> courses;

    private Course course;

    @FXML
    private VBox allCourses;

    @FXML
    private GridPane courseGrid;

    @FXML
    private void initialize() {
        flashcardDAO = new SqliteFlashcardDAO();
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();
        if (courses.isEmpty()) {
            courseDAO.insertSampleData();
            courses = courseDAO.getAllCourses();
        }

        // tighter spacing & padding on the VBox
        allCourses.setSpacing(6);                  // was 12 in FXML
        allCourses.setPadding(new Insets(6));      // override "-fx-padding: 12" if needed

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(8);
        row.setMaxWidth(Double.MAX_VALUE);         // let row stretch with the VBox
        // VBox.setVgrow(row, Priority.NEVER);     // default; don't expand rows vertically

        for (int i = 0; i < courses.size(); i++) {
            final int idx = i;
            Button btn = new Button(courses.get(idx).getTitle());

            btn.setOnAction(e -> {
                try {
                    course = courses.get(idx);
                    Course.setTransferredTitle(course.getTitle());
                    onCourse();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            btn.setMinWidth(0);
            btn.setMaxWidth(Double.MAX_VALUE);
            GridPane.setFillWidth(btn, true);
            GridPane.setHgrow(btn, Priority.ALWAYS);

            int col = i % 3;
            int rowss = i / 3;
            GridPane.setColumnIndex(btn, col);
            GridPane.setRowIndex(btn, rowss);

            courseGrid.getChildren().add(btn);

        }
    }



    private void onCourse() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createFlashcard-view.fxml"));
        Parent root = fxmlLoader.load();                 // must load before getController()
        CreateFlashcardController b = fxmlLoader.getController();
        b.setCourse(course);
        // pass whatever you need
        Scene scene = new Scene(root, HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    protected void onCreateCourse() throws IOException {
        String courseInput = courseField.getText();
        if (courseInput.trim().isEmpty()){
            return;
        }
        Course newCourse = new Course(courseInput);
        courseDAO.addCourse(newCourse);
        initialize();
    }

    @FXML Button createCourse;



    @FXML Button back;
    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

}
