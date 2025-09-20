package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class test {
    public Button submitButton;
    public Label errorQuizLabel;
    public TextField answerField;
    public TextField optionDField;
    public TextField optionCField;
    public TextField optionBField;
    public TextField optionAField;
    public TextField questionField;
    @FXML
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

    private void SelectedQuiz(Quiz question) {
        questionListView.getSelectionModel().select(question);
        questionField.setText(question.getQuizQuestion());
        optionAField.setText((question.getAnswer1()));
        optionBField.setText(question.getAnswer2());
        optionCField.setText(question.getAnswer3());
        optionDField.setText(question.getAnswer4());
        answerField.setText(question.getCorrectAnswer());
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @return The rendered cell.
     */
    @FXML
    private ListCell<Quiz> renderCell(ListView<Quiz> questionListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */

            private void onCourseSelected(MouseEvent mouseEvent) {
                ListCell<Quiz> clickedCell = (ListCell<Quiz>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Quiz selectedQuiz = clickedCell.getItem();
                if (selectedQuiz != null) SelectedQuiz(selectedQuiz);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param question The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Quiz question, boolean empty) {
                super.updateItem(question, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || question == null || question.getQuizQuestion() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onCourseSelected);
                } else {
                    setText(question.getQuizQuestion());
                }
            }
        };

    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncCourse() {

        questionListView.getItems().clear();
        List<Quiz> questions = quizDAO.getAllQuestionsfromCourse(course.getTitle());
        //boolean hasCourse = !courses.isEmpty();
        questionListView.getItems().addAll(questions);

        // Show / hide based on whether there are contacts
        //ontactContainer.setVisible(hasContact);
    }

    @FXML
    public void initializeQuizList() {
        questionListView.setCellFactory(this::renderCell);
        syncCourse();
        // Select the first contact and display its information
        questionListView.getSelectionModel().selectFirst();
//        Course course = questionListView.getSelectionModel().getSelectedItem();
//        if (course != null) {
//            selectCourse(course);
//        }
    }

}
