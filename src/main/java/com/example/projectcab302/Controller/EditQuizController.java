package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class EditQuizController extends BaseCourseAndSession {

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

    private ICoursesDAO courseDAO;

    @FXML
    private IQuizDAO quizDAO;

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
     * Synchronizes the contacts list view with the contacts in the database.
     */
    @FXML
    public void initialize() {
        questionListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setText(null);
                } else {
                    setText(quiz.getQuizQuestion());
                }
            }
        });

        // Listen for selection changes and update input fields accordingly
        questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldQuiz, selectedQuiz) -> {
            if (selectedQuiz != null) {
                questionField.setText(selectedQuiz.getQuizQuestion());
                optionAField.setText(selectedQuiz.getAnswer1());
                optionBField.setText(selectedQuiz.getAnswer2());
                optionCField.setText(selectedQuiz.getAnswer3());
                optionDField.setText(selectedQuiz.getAnswer4());
                answerField.setText(selectedQuiz.getCorrectAnswer());
            }
        });
    }

    private void syncCourseQuestions() {
        if (course == null) return;  // Safety check

        List<Quiz> quizzes = course.getQuizzes();
        ObservableList<Quiz> quizItems = FXCollections.observableArrayList(quizzes);
        questionListView.setItems(quizItems);
    }

    @Override
    public void afterCourseisSet() {
        if (course == null) return;  // Safety check

        List<Quiz> quizzes = course.getQuizzes();
        ObservableList<Quiz> quizItems = FXCollections.observableArrayList(quizzes);
        questionListView.setItems(quizItems);
    }

    public void onBack(ActionEvent actionEvent) {
        SceneManager.switchTo("quiz.fxml", this.user);
    }

    @FXML
    private void onCreateQuiz() {
        Quiz newQuestion = new Quiz(
                course.getTitle(),
                questionField.getText(),
                optionAField.getText(),
                optionBField.getText(),
                optionCField.getText(),
                optionDField.getText(),
                answerField.getText()
        );

        quizDAO.addQuiz(newQuestion);

        syncCourseQuestions();
        SelectedQuiz(newQuestion);
    }
    
    @FXML
    private void onEditQuiz() {
        Quiz newQuestion = questionListView.getSelectionModel().getSelectedItem();
        System.out.println("test edit");
        System.out.println(newQuestion);
        System.out.println("\n\n");

        if (newQuestion != null) {
            newQuestion.setQuizQuestion(questionField.getText());
            newQuestion.setAnswer1(optionAField.getText());
            newQuestion.setAnswer2(optionBField.getText());
            newQuestion.setAnswer3(optionCField.getText());
            newQuestion.setAnswer4(optionDField.getText());
            newQuestion.setCorrectAnswer(answerField.getText());

            quizDAO.updateQuiz(newQuestion);
            syncCourseQuestions();
            questionListView.getSelectionModel().select(newQuestion);
        }

    }
    @FXML
    private void onDeleteQuiz() {
        Quiz deletedQuiz = questionListView.getSelectionModel().getSelectedItem();
        System.out.println(deletedQuiz);
        System.out.println("@ del \n\n");

        if (deletedQuiz != null) {
            questionField.clear();
            optionAField.clear();
            optionBField.clear();
            optionCField.clear();
            optionDField.clear();
            answerField.clear();

            quizDAO.deleteQuiz(deletedQuiz);
            syncCourseQuestions();
        }
    }
}
