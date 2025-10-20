package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Submission;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.*;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentQuizController extends BaseCourseAndSession{
    // ───────────── Layouts ─────────────
    @FXML
    private VBox allCourses;
    @FXML private VBox allQuestions;
    @FXML private GridPane courseGrid;
    @FXML private HBox courseBox;
    @FXML private HBox submitWindow;

    // ───────────── Buttons ─────────────
    @FXML
    Button createCourse;
    @FXML Button back;
    @FXML Button submit;

    // ───────────── Texts ─────────────
    @FXML private TextField courseField;
    @FXML private Text score;

    // ─────── Controller state (non-UI, no @FXML) ───────
    private ICoursesDAO courseDAO;
    private List<Course> courses;
    private List<String> selectedAnswers = new ArrayList<>();

    private IQuizDAO quizDAO;
    private List<Quiz> quizzes;



    /**
     * Initializes the controller.
     * <p>
     * This method is automatically called after the FXML view is loaded.
     * It initializes the {@link ICoursesDAO}, retrieves courses from the database,
     * inserts sample data if the database is empty, and populates the courses grid.
     * </p>
     */
    @FXML
    private void initialize() {

    }

    @Override
    public void afterCourseisSet() {
        quizDAO = new SqlQuizDAO();
        quizzes = course.getQuizzes();

        while (selectedAnswers.size() < quizzes.size()) selectedAnswers.add(null);

        createCoursesGrid();
    }

    /**
     * Populates the grid with buttons for all available courses.
     * <p>
     * Each course is displayed as a button. Clicking a button switches
     * the scene to the flashcard creation view for that course.
     * </p>
     */
    private void createCoursesGrid() {
        // Ensure the grid is empty before repopulating (optional improvement)
        allQuestions.getChildren().clear();

        for (int i = 0; i < quizzes.size(); i++) {
            final int idx = i;

            // Create a button for each course
            Text question = new Text(quizzes.get(idx).getQuizQuestion());

            Button btn1 = new Button(quizzes.get(idx).getAnswer1());
            Button btn2 = new Button(quizzes.get(idx).getAnswer2());
            Button btn3 = new Button(quizzes.get(idx).getAnswer3());
            Button btn4 = new Button(quizzes.get(idx).getAnswer4());


            String correctAnswer = quizzes.get(idx).getCorrectAnswer();
            btn1.setOnAction(e ->{
                onButtonSelect(btn1, btn1, btn2, btn3, btn4);
                storeAnswer(quizzes.get(idx).getAnswer1(), idx);
            });
            btn2.setOnAction(e -> {
                onButtonSelect(btn2, btn1, btn2, btn3, btn4);
                storeAnswer(quizzes.get(idx).getAnswer2(), idx);
            });
            btn3.setOnAction(e -> {
                onButtonSelect(btn3, btn1, btn2, btn3, btn4);
                storeAnswer(quizzes.get(idx).getAnswer3(), idx);
            });
            btn4.setOnAction(e -> {
                onButtonSelect(btn4, btn1, btn2, btn3, btn4);
                storeAnswer(quizzes.get(idx).getAnswer4(), idx);
            });

            /*
            btn1.setOnAction(e -> checkAnswer(quizzes.get(idx).getAnswer1(), correctAnswer, btn1));
            btn2.setOnAction(e -> checkAnswer(quizzes.get(idx).getAnswer2(), correctAnswer, btn2));
            btn3.setOnAction(e -> checkAnswer(quizzes.get(idx).getAnswer3(), correctAnswer, btn3));
            btn4.setOnAction(e -> checkAnswer(quizzes.get(idx).getAnswer4(), correctAnswer, btn4));
            */

            // Let button expand in its grid cell
            btn1.setMinWidth(0);
            btn1.setMaxWidth(Double.MAX_VALUE);
            btn2.setMinWidth(0);
            btn2.setMaxWidth(Double.MAX_VALUE);
            btn3.setMinWidth(0);
            btn3.setMaxWidth(Double.MAX_VALUE);
            btn4.setMinWidth(0);
            btn4.setMaxWidth(Double.MAX_VALUE);

            allQuestions.getChildren().add(question);
            // Add button to the grid
            allQuestions.getChildren().add(btn1);
            allQuestions.getChildren().add(btn2);
            allQuestions.getChildren().add(btn3);
            allQuestions.getChildren().add(btn4);
        }
    }

    public void onButtonSelect(Button btn, Button btn1, Button btn2, Button btn3, Button btn4){
        btn1.setStyle("");
        btn2.setStyle("");
        btn3.setStyle("");
        btn4.setStyle("");

        btn.setStyle("-fx-background-color: grey; -fx-text-fill: black;");

    }

    public void storeAnswer(String answer, int index){
        selectedAnswers.set(index, answer);
    }

    public void onSubmit(){
        int tally = 0;

        System.out.println(selectedAnswers);
        for (int i = 0; i < quizzes.size(); i++){
            if (Objects.equals(quizzes.get(i).getCorrectAnswer(), selectedAnswers.get(i))){
                tally++;
                quizDAO.updateQuizMetrics(quizzes.get(i), true);
            }else{
                quizDAO.updateQuizMetrics(quizzes.get(i), false);
            }
        }




        score.setText(tally + "/" + quizzes.size());
    }


    /**
     * Handles the action when the "Create Course" button is pressed.
     * <p>
     * Reads the course name from the {@link #courseField},
     * validates that it is not empty, creates a new {@link Course},
     * and adds it to the database. Finally, it refreshes the course grid.
     * </p>
     *
     * @throws IOException if loading the view fails
     */
    @FXML
    private void onCreateCourse() throws IOException {
        String courseInput = courseField.getText();

        // Validate input
        if (courseInput.trim().isEmpty()) {
            return;
        }

        // Create and add the new course
        Course newCourse = new Course(courseInput);
        courseDAO.addCourse(newCourse);

        // Refresh the course grid
        initialize();
    }

    /**
     * Handles the action when the "Back" button is pressed.
     * <p>
     * Navigates the user back to the main teacher view.
     * </p>
     *
     * @throws IOException if loading the view fails
     */
    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("student-view.fxml", this.user);
    }


}
