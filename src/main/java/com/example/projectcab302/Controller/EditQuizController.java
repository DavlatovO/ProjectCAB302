package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.io.*;
/**
 * Controller for the "Edit Quiz" view.
 * <p>
 * Allows teachers to create, edit, delete, and manage quiz questions
 * associated with a specific {@link Course}. Integrates with
 * {@link SqlQuizDAO} for database operations and supports AI-assisted
 * false answer generation using a local Ollama model.
 * <p>
 * Features include:
 * <ul>
 *   <li>Displaying and selecting existing quiz questions.</li>
 *   <li>Creating, updating, and deleting quiz entries.</li>
 *   <li>Auto-generating false answers via local LLM.</li>
 *   <li>Randomizing multiple-choice answer order while maintaining correctness mapping.</li>
 * </ul>
 * This controller extends {@link BaseCourseAndSession} to manage both the current
 * {@link com.example.projectcab302.Model.User} session and associated course context.
 */
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

    @FXML
    private Text aiResponse;

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
        SceneManager.switchTo("courses-view.fxml", this.user, "Edit-Quiz.fxml");

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
                answerField.getText(),
                0,
                0
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

    /**
     * Generates three false answers for a multiple choice question via local LLM (Ollama).
     * <p>
     * Performs an HTTP POST to {@code http://localhost:11434/api/generate} with a JSON prompt,
     * parses the JSON response, updates {@code optionBField, optionCField, optionDField} with false answers that.
     * generate based on the question and correct answer inputs.
     * </p>
     * <p>
     * Network work is executed asynchronously via {@link CompletableFuture} and
     * UI updates are marshaled back to the FX thread via {@link Platform#runLater(Runnable)}.
     * </p>
     */
    @FXML
    private void genFalseAnswers() {

        if (questionField.getText() == null || questionField.getText().isBlank()) {
            Platform.runLater(() -> aiResponse.setText("Please fill the question field before trying to autocomplete answers."));
            return;
        }

        if (optionAField.getText() == null || optionAField.getText().isBlank()) {
            Platform.runLater(() -> aiResponse.setText("Please fill the Option A field with the correct answer to the question."));
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String prompt =
                        "Generate 3 false but believable answers for the multiple choice question: " + questionField.getText() + ".\n" +
                                "The correct answer is: " + optionAField.getText() + ".\n" +
                                "Return ONLY valid JSON of the exact form: " +
                                "{\"false1\": A, \"false2\": B, \"false3\": C} " +
                                "where A, B, and C are different false but believable answers. " +
                                "Do not include the correct answer or any explanation.";

                String body = """
                    {
                      "model": "llama3.1:latest",
                      "prompt": %s,
                      "stream": false,
                      "keep_alive": "30m",
                      "format": "json",
                      "options": {
                        "temperature": 0.7,
                        "top_k": 50,
                        "top_p": 0.9
                      }
                    }
                    """.formatted(JSONObject.quote(prompt));

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int code = conn.getResponseCode();
                System.out.println("HTTP Status Code: " + code);
                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                String resp = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines().reduce("", (a, b) -> a + b);

                conn.disconnect();

                if (code >= 200 && code < 300) {
                    JSONObject outer = new JSONObject(resp);
                    String responseText = outer.getString("response");

                    // Parse the false answers from the model's response
                    JSONObject falseAnswers = new JSONObject(responseText);

                    // Currently only checks if option fields B, C, D are empty.
                    if (optionBField.getText().isBlank() && optionCField.getText().isBlank() && optionDField.getText().isBlank()) {
                        Platform.runLater(() -> {
                            optionBField.setText(falseAnswers.optString("false1", ""));
                            optionCField.setText(falseAnswers.optString("false2", ""));
                            optionDField.setText(falseAnswers.optString("false3", ""));
                            answerField.setText("A");
                            aiResponse.setText("False answers generated successfully.");
                        });
                    } else {
                        Platform.runLater(() -> aiResponse.setText("Options B, C, and D must be empty before generating false answers."));
                    }
                } else {
                    Platform.runLater(() -> aiResponse.setText("Error: " + resp));
                }

            } catch (Exception e) {
                Platform.runLater(() -> aiResponse.setText("Error: " + e.getMessage()));
            }
        });
    }

    /**
     * Shuffles multiple choice answers and automatically updates answerField to remain consistent with the shuffle.
     */
    @FXML
    private void randomizeAnswers() {

        if (optionAField.getText().isBlank() && optionBField.getText().isBlank() && optionCField.getText().isBlank() && optionDField.getText().isBlank()) {
            Platform.runLater(() -> aiResponse.setText("No answers to shuffle."));
            return;
        }

        //create arraylist with answers from input field
        ArrayList<String> answers = new ArrayList<>();
        answers.add(optionAField.getText());
        answers.add(optionBField.getText());
        answers.add(optionCField.getText());
        answers.add(optionDField.getText());

        String correctLetter = answerField.getText().trim().toUpperCase();

        int correctIndex = switch (correctLetter) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            default -> -1; // fallback if invalid
        };

        if (correctIndex == -1) {
            aiResponse.setText("Invalid correct answer in Answer Field.");
            return;
        }
        String correctAnswer = answers.get(correctIndex);

        //shuffle answers list
        Collections.shuffle(answers);

        //set shuffled answers
        optionAField.setText(answers.get(0));
        optionBField.setText(answers.get(1));
        optionCField.setText(answers.get(2));
        optionDField.setText(answers.get(3));

        int newCorrectIndex = answers.indexOf(correctAnswer);
        String newCorrectLetter = switch (newCorrectIndex) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            default -> "?";
        };

        answerField.setText(newCorrectLetter);
    }

    @FXML
    private void onQuizMetrics(){
        SceneManager.switchTo("quizMetrics.fxml", this.user, this.course);
    }

}
