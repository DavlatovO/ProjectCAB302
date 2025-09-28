package com.example.projectcab302.Controller;

import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CoursesController extends BaseSession{
    // ───────────── Layouts ─────────────
    @FXML private VBox allCourses;
    @FXML private GridPane courseGrid;
    @FXML private HBox courseBox;

    // ───────────── Buttons ─────────────
    @FXML Button createCourse;
    @FXML Button back;

    // ───────────── Texts ─────────────
    @FXML private TextField courseField;

    // ─────── Controller state (non-UI, no @FXML) ───────
    private ICoursesDAO courseDAO;
    private List<Course> courses;
    private Course course;


    // At the start of the program, get all courses
    @FXML
    private void initialize() {

        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();
        if (courses.isEmpty()) {
            courseDAO.insertSampleData();
            courses = courseDAO.getAllCourses();
        }

        createCoursesGrid();
    }

    // Creates a grid of all courses in the db
    private void createCoursesGrid() {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(8);
        row.setMaxWidth(Double.MAX_VALUE);         // let row stretch with the VBox


        for (int i = 0; i < courses.size(); i++) {
            final int idx = i;
            Button btn = new Button(courses.get(idx).getTitle());

            btn.setOnAction(e -> {
                SceneManager.switchTo("createFlashcard-view.fxml", this.user, courses.get(idx));

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



    // When create course is pressed, add a course with name inputed in the text field
    @FXML
    private void onCreateCourse() throws IOException {
        String courseInput = courseField.getText();
        if (courseInput.trim().isEmpty()){
            return;
        }
        Course newCourse = new Course(courseInput);
        courseDAO.addCourse(newCourse);
        initialize();
    }

    // When back button is pressed, go back to main menu
    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) back.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }



}
