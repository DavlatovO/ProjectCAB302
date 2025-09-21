package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.lang.String;
import java.util.List;



public class CreateQuizController {

    @FXML
    public Button submitButton;
    @FXML
    public Label Question;
    @FXML
    public Label QuizSelect;
    public Button startQuiz;

    @FXML
    private RadioButton optionAField;
    @FXML
    private RadioButton optionBField;
    @FXML
    private RadioButton optionCField;
    @FXML
    private RadioButton optionDField;
    @FXML
    private TextField answerField;
    @FXML
    private Label errorQuizLabel;
    @FXML
    private IQuizDAO quizDAO;
    @FXML
    private ICoursesDAO coursesDAO;
    @FXML
    private ListView<Course> courseListView;
    @FXML
    private String courseName;

    public CreateQuizController() {

    }

    /**
     * Programmatically selects a contact in the list view and
     * updates the text fields with the contact's information.
     * @param course The contact to select.
     */
    private void selectCourse(Course course) {
        courseListView.getSelectionModel().select(course);
        courseName = course.getTitle();
        //System.out.println(courseName);
        QuizSelect.setText(courseName);
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param courseListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Course> renderCell(ListView<Course> courseListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */

            private void onCourseSelected(MouseEvent mouseEvent) {
                ListCell<Course> clickedCell = (ListCell<Course>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Course selectedCourse = clickedCell.getItem();
                if (selectedCourse != null) selectCourse(selectedCourse);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param course The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || course == null || course.getTitle() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onCourseSelected);
                } else {
                    setText(course.getTitle());
                }
            }
        };

    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncCourse() {
        coursesDAO = new SqliteCoursesDAO();

        courseListView.getItems().clear();
        List<Course> courses = coursesDAO.getAllCourses();
        //boolean hasCourse = !courses.isEmpty();
        courseListView.getItems().addAll(courses);

        // Show / hide based on whether there are contacts
        //ontactContainer.setVisible(hasContact);
    }

//    @FXML
//    public void initialize() {
//
//
//        courseListView.setCellFactory(this::renderCell);
//        syncCourse();
//        // Select the first contact and display its information
//        courseListView.getSelectionModel().selectFirst();
//        Course course = courseListView.getSelectionModel().getSelectedItem();
//        if (course != null) {
//            selectCourse(course);
//        }
//    }


    @FXML
    protected void submitQuiz(ActionEvent actionEvent) {
    }

    @FXML
    Button backButton;
    @FXML
    protected void onBack() {
        SceneManager.switchTo("teacher-view.fxml");
    }

    public static int score = 0;

    @FXML
    public void onStartQuiz(ActionEvent actionEvent) {
        Course chosenCourse = courseListView.getSelectionModel().getSelectedItem();

        startQuiz.setVisible(false);
        quizDAO = new SqlQuizDAO();
        courseListView.setVisible(false);
        List<Quiz> questions = quizDAO.getAllQuestionsfromCourse(chosenCourse.getTitle());
        System.out.println(questions);
        ToggleGroup group = new ToggleGroup();
        optionAField.setToggleGroup(group);
        optionBField.setToggleGroup(group);
        optionCField.setToggleGroup(group);
        optionDField.setToggleGroup(group);


        for (Quiz question : questions) {
            optionAField.setText(question.getAnswer1());
            optionBField.setText(question.getAnswer2());
            optionCField.setText(question.getAnswer3());
            optionDField.setText(question.getAnswer4());
            Question.setText(question.getQuizQuestion());
            RadioButton selected_radial = (RadioButton) group.getSelectedToggle();
            String toggledValue = selected_radial.getText();

            System.out.println(question);
//            submitButton.setOnAction(e -> {
//                 if (toggledValue.equals(question.getCorrectAnswer())) {
//                     score = score + 1;
//
//                     errorQuizLabel.setText("Well done");
//                 }
//                 if (toggledValue != question.getCorrectAnswer()) {
//                     errorQuizLabel.setText("Unlucky");
//                 }
//                 System.out.println(score);
//
//            });
        }
    }


//    public void onSubmitAnswer(ActionEvent actionEvent) {
//        int i = 1;
//        i = i+1;
//        Course chosenCourse = courseListView.getSelectionModel().getSelectedItem();
//        quizDAO = new SqlQuizDAO();
//        courseListView.setVisible(false);
//        List<Quiz> questions = quizDAO.getAllQuestionsfromCourse(chosenCourse.getTitle());
//        Quiz question = questions.get(i);
//
//        System.out.println(questions);
//        ToggleGroup group = new ToggleGroup();
//        optionAField.setToggleGroup(group);
//        optionBField.setToggleGroup(group);
//        optionCField.setToggleGroup(group);
//        optionDField.setToggleGroup(group);
//        RadioButton selected_radial = (RadioButton) group.getSelectedToggle();
//        String toggledValue = selected_radial.getText();
//
//        if (toggledValue.equals(question.getCorrectAnswer())) {
//            score = score + 1;
//
//            errorQuizLabel.setText("Well done");
//        }
//        if (toggledValue != question.getCorrectAnswer()) {
//            errorQuizLabel.setText("Unlucky");
//        }
//        System.out.println(score);


}
