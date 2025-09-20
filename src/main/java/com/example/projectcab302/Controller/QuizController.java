package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.util.List;


public class QuizController {
    public static String passedcourse;
    public Button submitButton;
    public Label errorQuizLabel;
    public TextField answerField;
    public TextField optionDField;
    public TextField optionCField;
    public TextField optionBField;
    public TextField optionAField;
    public TextField questionField;
    public ListView<Quiz> questionListView;
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
    
            btn.setOnAction(e -> {
                try {
                    course = courses.get(idx);
                    //Course.setTransferredTitle(course.getTitle());
                    setPassedcourse(course.getTitle());
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
    @FXML
    private void onCourse() throws IOException{
        //allCourses.setManaged(false);
        //allCourses.setVisible(false);
        SceneManager.switchTo("Edit-Quiz.fxml");

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

    public void submitQuiz(ActionEvent actionEvent) {
    }
}
