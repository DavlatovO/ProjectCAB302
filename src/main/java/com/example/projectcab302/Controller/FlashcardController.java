package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.SceneManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for reviewing and practicing flashcards for a selected {@link Course}.
 * <p>
 * Features:
 * <ul>
 *     <li>Preview mode: browse cards, flip for answer.</li>
 *     <li>PvP mode: timed rounds with automated scoring via a local LLM (Ollama).</li>
 *     <li>Navigation to modify cards and back to menu.</li>
 * </ul>
 * </p>
 */
public class FlashcardController extends BaseCourseAndSession {

    // ───────────── Layouts ─────────────

    /** Container for preview-only controls (visible in preview mode). */
    @FXML private HBox previewOptions;

    /** Container for PvP-only controls (visible in PvP mode). */
    @FXML private HBox pvpOptions;

    /** The card "face" container used for flip animation. */
    @FXML private StackPane cubeGroup;

    /** Time limit indicator used in PvP rounds. */
    @FXML private ProgressBar bar;

    // ───────────── Buttons ─────────────

    /** Go to next flashcard. */
    @FXML private Button next;

    /** Go to previous flashcard. */
    @FXML private Button prev;

    /** Submit an answer during a PvP round. */
    @FXML private Button pvpSubmitButton;

    /** Indicate the player is ready to start the next PvP round. */
    @FXML private Button ready;

    /** Flip the current card between question and answer. */
    @FXML private Button flipButton;

    /** Submit an answer in preview mode (for AI scoring). */
    @FXML private Button submitButton;

    /** Open the flashcard edit view for this course. */
    @FXML private Button modifyButton;

    // ───────────── Texts ─────────────

    /** Displays the current question (or the countdown temporarily). */
    @FXML private Text question;

    /** Displays the current answer after flipping. */
    @FXML private Text test;

    /** Displays the current card index like "2/10". */
    @FXML private Text cardNum;

    /** Player 1 score display. */
    @FXML private Text player1;

    /** Player 2 score display. */
    @FXML private Text player2;

    /** Countdown and PvP status messages. */
    @FXML private Text countdown;

    /** Shows AI feedback after submitting an answer. */
    @FXML private Text aiResponse;

    /** Input area for the user's answer. */
    @FXML private TextArea answer;

    // ─────── Controller state (non-UI, no @FXML) ───────

    /** DAO for flashcard persistence (injected elsewhere). */
    private IFlashcardDAO flashcardDAO;

    /** All flashcards for the active course. */
    private List<Flashcard> flashcards;

    /** True if the answer side is currently shown; false if question side. */
    private boolean cardShowsAnswer = true;

    /** Index of the current flashcard. */
    private int cardCount = 0;

    /** Timeline controlling the PvP round progress bar. */
    private Timeline fill;

    // PvP state
    /** Whether PvP mode is active. */
    private boolean pvpActive = false;

    /** True if it's Player 1's turn. */
    private boolean p1Turn = true;

    /** True if it's Player 2's turn. */
    private boolean p2Turn = false;

    /** Player 1 cumulative score. */
    private int p1Score = 0;

    /** Player 2 cumulative score. */
    private int p2Score = 0;

    /**
     * Initializes the controller after FXML has been loaded.
     * <p>
     * Sets initial visibility and disabled states for PvP/preview controls.
     * </p>
     */
    @FXML
    private void initialize() {
        pvpOptions.setVisible(false);

        player1.setVisible(false);
        player2.setVisible(false);
        countdown.setVisible(false);
        pvpActive = false;
        previewOptions.setVisible(true);

        ready.setVisible(false);
        bar.setVisible(false);
        flipButton.setVisible(true);
        flipButton.setDisable(false);
    }

    /**
     * Called by the base class after {@link #course} is assigned.
     * <p>
     * Prepares the first card and updates UI labels.
     * </p>
     */
    @Override
    public void afterCourseisSet() {
        flipCard();

        flashcards = course.getFlashcards();
        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    /**
     * Enables PvP mode and sets up the first round.
     * <p>
     * Resets scores, shows PvP UI, hides preview controls, and starts the countdown.
     * </p>
     */
    @FXML
    private void onPvP() {
        bar.setVisible(true);
        ready.setVisible(true);
        cardCount = 0;
        p1Score = 0;
        p2Score = 0;
        player1.setVisible(true);
        player2.setVisible(true);
        player1.setText("player 1: " + p1Score);
        player2.setText("player 1: " + p1Score);
        player2.setVisible(true);

        previewOptions.setVisible(false);
        pvpOptions.setVisible(true);
        pvpActive = true;
        p1Turn = true;
        flipButton.setVisible(false);

        startRound();
        System.out.println("onPvP clicked");
    }

    /**
     * Starts a single PvP round with a 3-second countdown, then enables answering.
     * <p>
     * Displays 3→2→1 countdown, reveals the question, and starts the 10-second timer bar.
     * </p>
     */
    @FXML
    private void startRound() {
        ready.setDisable(true);
        pvpSubmitButton.setDisable(true);
        countdown.setVisible(true);

        int start = 3;
        IntegerProperty seconds = new SimpleIntegerProperty(start);

        // Bind the countdown number into the question text for display
        question.textProperty().bind(seconds.asString());

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> seconds.set(seconds.get() - 1))
        );
        tl.setCycleCount(start);

        tl.setOnFinished(e -> {
            pvpSubmitButton.setDisable(false);
            question.textProperty().unbind();
            question.setText(flashcards.get(cardCount).getQuestion());
            submitButton.setDisable(false);
            startProgressBar();
        });

        tl.play();
    }

    /**
     * Handles a PvP submission:
     * <ul>
     *     <li>Stops the timer if running.</li>
     *     <li>Scores the answer (or gives 0 if blank).</li>
     *     <li>Flips the card to show the answer and switches turns.</li>
     * </ul>
     */
    @FXML
    private void onPvpSubmit() {
        if (fill != null && fill.getStatus() == Animation.Status.RUNNING) {
            fill.stop(); // onFinished will NOT fire
        }

        String response = answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Score 0/5, no answer given");
            flipCard();
            switchTurns();
        } else {
            onSubmit();
        }

        ready.setDisable(false);
        pvpSubmitButton.setDisable(true);
        answer.setText("");
    }

    /**
     * Checks whether the match has ended and displays the winner if so.
     *
     * @return {@code true} if a winner was determined and the game ended; otherwise {@code false}
     */
    private boolean checkWinner() {
        if (cardCount == (flashcards.size() - 1) && p2Turn) {
            if (p1Score > p2Score) {
                countdown.setText("player 1 won");
            } else {
                countdown.setText("player 2 won");
            }
            ready.setDisable(true);
            pvpSubmitButton.setDisable(true);
            return true;
        }
        return false;
    }

    /**
     * Switches the active player turn and updates the countdown message.
     */
    private void switchTurns() {
        if (p1Turn) {
            countdown.setText("Give Laptop to player 2");
            p1Turn = false;
            p2Turn = true;
        } else if (p2Turn) {
            countdown.setText("Give Laptop to player 1");
            p1Turn = true;
            p2Turn = false;
        }
    }

    /**
     * Signals readiness for the next PvP turn.
     * <p>
     * Clears messages, flips card back to question side, advances card on P1 turns,
     * and starts the next round.
     * </p>
     */
    @FXML
    private void onReady() {
        aiResponse.setText("");
        countdown.setText("");
        flipCard();
        if (p1Turn) {
            cardCount++;
            cardNum.setText((cardCount + 1) + "/" + flashcards.size());
        }
        startRound();
    }

    /**
     * Starts the timed progress bar for a PvP round (10 seconds).
     * <p>
     * When the timer ends, it auto-triggers {@link #onPvpSubmit()}.
     * </p>
     */
    private void startProgressBar() {
        if (fill != null) fill.stop();
        bar.setProgress(0);

        fill = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(10), new KeyValue(bar.progressProperty(), 1))
        );

        fill.setOnFinished(e -> {
            pvpSubmitButton.setDisable(true);
            onPvpSubmit();
        });

        fill.playFromStart();
    }

    /**
     * Navigates to the previous flashcard (wraps around to last).
     * <p>
     * Ensures the card shows the question side after navigation.
     * </p>
     */
    @FXML
    private void prevCard() {
        if (cardCount == 0) {
            cardCount = flashcards.size() - 1;
        } else {
            cardCount--;
        }

        if (cardShowsAnswer) {
            flipCard();
        }

        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    /**
     * Navigates to the next flashcard (wraps around to first).
     * <p>
     * Ensures the card shows the question side after navigation.
     * </p>
     */
    @FXML
    private void nextCard() {
        if (cardCount == flashcards.size() - 1) {
            cardCount = 0;
        } else {
            cardCount++;
        }

        if (cardShowsAnswer) {
            flipCard();
        }
        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    /**
     * Flips the card with a Y-axis rotation animation between question and answer.
     * <p>
     * Uses a short {@link RotateTransition} and a mid-animation {@link PauseTransition}
     * to update the displayed text at the flip's halfway point.
     * </p>
     */
    @FXML
    private void flipCard() {
        float duration = 0.25F;

        RotateTransition spin = new RotateTransition(Duration.seconds(duration), cubeGroup);
        spin.setAxis(Rotate.Y_AXIS);
        spin.setByAngle(180);

        System.out.println(cardCount);
        System.out.println(cardCount);

        PauseTransition halfway = new PauseTransition(Duration.seconds(duration / 4));
        if (cardShowsAnswer) {
            // Currently showing answer → hide it to show question next
            halfway.setOnFinished(e -> test.setText(""));
            cardShowsAnswer = false;
        } else {
            // Currently showing question → switch to answer
            halfway.setOnFinished(e -> test.setText(flashcards.get(cardCount).getAnswer()));
            cardShowsAnswer = true;
        }

        new ParallelTransition(spin, halfway).play();
    }

    /**
     * Opens the flashcard edit view for the current course.
     *
     * @throws IOException if the edit view cannot be loaded
     */
    @FXML
    private void onModifyFlashcard() throws IOException {
        SceneManager.switchTo("createFlashcard-view.fxml", this.user, this.course);
    }

    /**
     * Navigates back to the teacher menu view.
     *
     * @throws IOException if the teacher view cannot be loaded
     */
    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("teacher-view.fxml", this.user);
    }

    /**
     * Submits the user's answer to a local LLM (Ollama) for scoring (1–5).
     * <p>
     * Performs an HTTP POST to {@code http://localhost:11434/api/generate} with a JSON prompt,
     * parses the JSON response, updates PvP scores if active, and shows a human-readable grade.
     * </p>
     * <p>
     * Network work is executed asynchronously via {@link CompletableFuture} and
     * UI updates are marshaled back to the FX thread via {@link Platform#runLater(Runnable)}.
     * </p>
     */
    @FXML
    private void onSubmit() {
        String response = answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Please enter valid answer");
            return;
        }

        String question = flashcards.get(cardCount).getQuestion();
        String answerInput = flashcards.get(cardCount).getAnswer();

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String prompt =
                        "You are scoring a student answer against a reference answer.\n" +
                                "Return ONLY valid JSON of the exact form {\"score\": N} where N is an integer 1–5.\n" +
                                "No prose, no code fences, no explanations.\n" +
                                "\n" +
                                "Scoring rubric (prioritize correctness and topical coverage; style/wording doesn’t matter):\n" +
                                "5: B is correct and covers the key points of A (allow synonyms/rephrasings) and has similar word count. \n" +
                                "4: Mostly correct with small omissions or minor inaccuracies.\n" +
                                "3: Partially correct; covers some key points but misses several.\n" +
                                "2: Barely related; only one minor point overlaps with A.\n" +
                                "1: Incorrect, off-topic, empty, or just repeats the question / says 'I don't know'.\n" +
                                "\n" +
                                "<QUESTION>\n" + question + "\n</QUESTION>\n" +
                                "<ANSWER_A>\n" + answerInput + "\n</ANSWER_A>\n" +
                                "<ANSWER_B>\n" + response + "\n</ANSWER_B>\n" +
                                "\n" +
                                "Output only: {\"score\": N}";

                String body = """
                        {
                          "model": "llama3.1:latest",
                          "prompt": %s,
                          "stream": false,
                          "keep_alive": "30m",
                          "format": "json",
                          "options": {
                            "temperature": 0,
                            "top_k": 1,
                            "top_p": 1,
                            "seed": 1234,
                            "num_predict": 8,
                            "num_thread": 8,
                            "num_batch": 256,
                            "num_gpu": 999
                            
                          }
                        }
                        """.formatted(JSONObject.quote(prompt), Runtime.getRuntime().availableProcessors());

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                String resp = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines().reduce("", (a, b) -> a + b);

                if (code >= 200 && code < 300) {
                    JSONObject outer = new JSONObject(resp);
                    String responseText = outer.getString("response");
                    int score = new JSONObject(responseText).getInt("score");

                    String grade;
                    if (pvpActive) {
                        submitButton.setDisable(true);
                        flipCard();
                        if (p1Turn) {
                            p1Score += score;
                            player1.setText("player 1: " + p1Score);
                        } else {
                            p2Score += score;
                            player2.setText("player 2: " + p2Score);
                        }
                        if (!checkWinner()) {
                            switchTurns();
                        }
                    }

                    if (score == 1) {
                        grade = " Not close to answer at all";
                    } else if (score == 2 || score == 3) {
                        grade = "Getting there!";
                    } else if (score == 4) {
                        grade = "Almost there!";
                    } else if (score == 5) {
                        grade = " Comparable to answer, good job!";
                    } else {
                        grade = " (score out of range)";
                    }

                    Platform.runLater(() -> aiResponse.setText("Score " + score + "/5 " + grade));
                } else {
                    Platform.runLater(() -> aiResponse.setText("Error: " + resp));
                }
                conn.disconnect();

            } catch (Exception e) {
                Platform.runLater(() -> aiResponse.setText("Error: " + e.getMessage()));
            }
        });
    }
}
