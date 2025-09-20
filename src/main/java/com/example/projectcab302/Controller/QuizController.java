package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;


public class QuizController {
    @FXML
    private GridPane courseQuizGrid;
    @FXML
    private VBox allCourses;
    @FXML
    private ICoursesDAO courseDAO;
    @FXML
    private List<Course> courses;
    @FXML
    private Course course;
    protected void saveQuiz() {

    }

    @FXML
    private void initialize(){
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
                    //Course.setTransferredTitle(course.getTitle());
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
    
            courseQuizGrid.getChildren().add(btn);

        }
    }

    private void onCourse() throws IOException{
        SceneManager.switchTo("edit-quiz.fxml");
    }

    @FXML
    protected void onBack() {
        SceneManager.switchTo("teacher-view.fxml"); //Need to edit later to make functional with student view
    }

    @FXML
    protected void createQuiz() {
        SceneManager.switchTo("create-quiz.fxml");
    }
    //Note bug where if create quiz is pressed twice the scene manager switches to Teacher-view.fxml


    public void editQuiz(ActionEvent actionEvent) {
    }

    public void deleteQuiz(ActionEvent actionEvent) {
    }
}
