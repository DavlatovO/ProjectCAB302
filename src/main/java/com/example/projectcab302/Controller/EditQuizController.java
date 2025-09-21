package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class EditQuizController {

    @FXML
    private Label errorQuizLabel;
    @FXML
    private TextField answerField;
    @FXML
    private TextField optionDField;
    @FXML
    private TextField optionCField;
    @FXML
    private TextField optionBField;
    @FXML
    private TextField optionAField;
    @FXML
    private TextField questionField;
    @FXML
    private ListView<Quiz> questionListView;

    @FXML
    private ICoursesDAO courseDAO;
    @FXML
    private Course course;
    @FXML
    private IQuizDAO quizDAO;
    @FXML
    private Quiz newquestion;

    public EditQuizController() {
        quizDAO = new SqlQuizDAO();
    }



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
        //System.out.println(QuizController.getPassedcourse());
        questionListView.getItems().clear();
        List<Quiz> questions = quizDAO.getAllQuestionsfromCourse(QuizController.getPassedcourse());
        //boolean hasCourse = !courses.isEmpty();
        System.out.println("\n\n");
        System.out.println(quizDAO.getAllQuestionsfromCourse(QuizController.getPassedcourse()));
        System.out.println(questions);
        System.out.println(" @ SYNC list of questions \n\n");
        questionListView.getItems().addAll(questions);

    }

    @FXML
    public void initialize() {
        questionListView.setCellFactory(this::renderCell);
        syncCourse();
        // Select the first contact and display its information
        questionListView.getSelectionModel().selectFirst();
        Quiz quiz = questionListView.getSelectionModel().getSelectedItem();
        if (quiz != null) {
            SelectedQuiz(quiz);
        }
        System.out.println(quiz);

        System.out.println("Quiz at init \n\n");
    }

    public void onBack(ActionEvent actionEvent) {
        SceneManager.switchTo("quiz.fxml");
    }

    @FXML
    private void onCreateQuiz() {
        //Quiz newquestion = new Quiz(questionField.getText(), optionAField.getText(),optionBField.getText(), optionCField.getText(), optionDField.getText(), answerField.getText(), QuizController.getPassedcourse());
        final String DEF_QUES = "Enter Quiz Question";
        final String DEF_A1 = "Option A";
        final String DEF_A2 = "Option B";
        final String DEF_A3 = "...";
        final String DEF_A4 = "...";
        final String DEF_A5 = "Answer A, B, C or D";
        Quiz newquestion = new Quiz(QuizController.getPassedcourse(), DEF_QUES, DEF_A1, DEF_A2, DEF_A3, DEF_A4, DEF_A5);

        System.out.println(newquestion);
        System.out.println("@ create quiz \n\n");

        quizDAO.addQuiz(newquestion);
        syncCourse();
        SelectedQuiz(newquestion);
        //questionField.requestFocus();
    }
    @FXML
    private void onEditQuiz() {
        Quiz newquestion = questionListView.getSelectionModel().getSelectedItem();
        System.out.println("test edit");
        System.out.println(newquestion);
        System.out.println(QuizController.getPassedcourse());

        System.out.println("\n\n");

        if (newquestion != null) {
            newquestion.setCourse(QuizController.getPassedcourse());
            newquestion.setQuizQuestion(questionField.getText());
            newquestion.setAnswer1(optionAField.getText());
            newquestion.setAnswer2(optionBField.getText());
            newquestion.setAnswer3(optionCField.getText());
            newquestion.setAnswer4(optionDField.getText());
            newquestion.setCorrectAnswer(answerField.getText());

            quizDAO.updateQuiz(newquestion);
            syncCourse();

        }

    }
    @FXML
    private void onDeleteQuiz() {
        Quiz deletedQuiz = questionListView.getSelectionModel().getSelectedItem();
        System.out.println(deletedQuiz);
        System.out.println("@ del \n\n");

        if (deletedQuiz != null) {
            quizDAO.deleteQuiz(deletedQuiz);
            syncCourse();
        }
    }
}
