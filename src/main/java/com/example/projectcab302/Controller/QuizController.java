package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;


public class QuizController extends BaseSession {
    public static String passedcourse;

    @FXML
    private VBox SelectedEdit;
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
    private IQuizDAO quizDAO;
    protected void saveQuiz() {

    }

    public static String getPassedcourse() {
        return passedcourse;
    }

    public static void setPassedcourse(String passedcourse) {
        QuizController.passedcourse = passedcourse;
    }

    @FXML
    public void initialize(){
        //SelectedEdit.managedProperty().bind(SelectedEdit.visibleProperty());
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();
        VBox.setVgrow(allCourses, Priority.SOMETIMES);
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
            //VBox.setVgrow(row, Priority.NEVER);     // default; don't expand rows vertically
    
            for (int i = 0; i < courses.size(); i++) {
            final int idx = i;
            Button btn = new Button(courses.get(idx).getTitle());
    
            btn.setOnAction(e -> SceneManager.switchTo("Edit-Quiz.fxml", user, courses.get(idx)));
    
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

    @FXML
    protected void onBack() {
        SceneManager.switchTo("teacher-view.fxml", this.user); //Need to edit later to make functional with student view
    }

    public void onDoQuiz(ActionEvent actionEvent) {
        SceneManager.switchTo("create-quiz.fxml", this.user);
    }
}
