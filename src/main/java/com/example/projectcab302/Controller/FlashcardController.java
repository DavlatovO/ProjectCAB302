package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
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


public class FlashcardController {
    // ───────────── Layouts ─────────────
    @FXML private HBox previewOptions;
    @FXML private HBox pvpOptions;
    @FXML private StackPane cubeGroup;
    @FXML private ProgressBar bar;

    // ───────────── Buttons ─────────────
    @FXML private Button next;
    @FXML private Button prev;
    @FXML private Button pvpSubmitButton;
    @FXML private Button ready;
    @FXML private Button flipButton;
    @FXML private Button submitButton;

    // ───────────── Texts ─────────────
    @FXML private Text question;
    @FXML private Text test;
    @FXML private Text cardNum;
    @FXML private Text player1;
    @FXML private Text player2;
    @FXML private Text countdown;
    @FXML private Text aiResponse;
    @FXML private TextArea answer;

    // ─────── Controller state (non-UI, no @FXML) ───────
    private IFlashcardDAO flashcardDAO;
    private List<Flashcard> flashcards;
    private Course course;

    // keeps track of card if flipped or not; if even clicks, answer not showing
    private boolean cardShowsAnswer = false;

    // which flashcard is currently showing
    private int cardCount = 0;

    // keeps track of progress bar
    private Timeline fill;

    // PvP state
    private boolean pvpActive = false;
    private boolean p1Turn = true;
    private boolean p2Turn = false;
    private int p1Score = 0;
    private int p2Score = 0;


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

    // Used for passing what course's flashcards should be seen here
    public void setCourse(Course Course) {
        cubeGroup.setRotationAxis(Rotate.Y_AXIS);
        cubeGroup.setRotate(180); // rotate 45° before showing

        this.course = Course;
        flashcards = course.getFlashcards();
        question.setText(flashcards.get(cardCount).getQuestion());

        cardNum.setText((cardCount + 1) + "/" + flashcards.size());


    }



    // When pvp button is pressed, activate pvp mode
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

    // pvp mode method for initiating a player's round
    @FXML
    private void startRound() {
        ready.setDisable(true);
        pvpSubmitButton.setDisable(true);
        // start 3 second count down and hide question
        countdown.setVisible(true);
        int start = 3;
        IntegerProperty seconds = new SimpleIntegerProperty(start);

        // show initial "3"
        question.textProperty().bind(seconds.asString());

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> seconds.set(seconds.get() - 1))
        );
        tl.setCycleCount(start); // run 3 ticks: 3→2→1→0 (finishes after hitting 0)

        tl.setOnFinished(e -> {
            pvpSubmitButton.setDisable(false);
            question.textProperty().unbind();
            question.setText(flashcards.get(cardCount).getQuestion());
            submitButton.setDisable(false);
            // start 10 second progress bar
            startProgressBar();
            // countdown.setVisible(false);  // if you want to hide it after
        });

        tl.play();
    }

    // when a user presses submit in pvp mode
    @FXML
    private void onPvpSubmit() {


        if (fill != null && fill.getStatus() == Animation.Status.RUNNING) {
            fill.stop();                 // onFinished WILL NOT fire
        }
        String response = answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Score 0/5, no answer given" );
            flipCard();
            switchTurns();
        } else{
            onSubmit();
        }


        ready.setDisable(false);
        pvpSubmitButton.setDisable(true);


        answer.setText("");

    }

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

    // changes who's turn it is
    private void switchTurns() {
        if (p1Turn){
            countdown.setText("Give Laptop to player 2");

            p1Turn = false;
            p2Turn = true;
        } else if (p2Turn){

            countdown.setText("Give Laptop to player 1");
            p1Turn = true;
            p2Turn = false;
        }
    }


    @FXML
    private void onReady() {
        aiResponse.setText("");
        countdown.setText("");
        flipCard();
        if (p1Turn){
            cardCount++;
            cardNum.setText((cardCount + 1) + "/" + flashcards.size());
        }
        startRound();


    }


    private void startProgressBar() {
        if (fill != null) fill.stop();
        bar.setProgress(0);

        fill = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(bar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(10), new KeyValue(bar.progressProperty(), 1))
        );

        fill.setOnFinished(e -> {

            pvpSubmitButton.setDisable(true);
            String response = answer.getText();
            onPvpSubmit();

        });

        fill.playFromStart();
    }

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

    @FXML
    private void nextCard() {

        if (cardCount == flashcards.size() - 1) {
            cardCount = 0;
        } else {
            cardCount++;
        }

        if (cardShowsAnswer) { //when odd, show back of card/ blank side
            flipCard();
        }
        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    // flips card
    @FXML
    private void flipCard() {


        float duration = 0.25F;


        RotateTransition spin = new RotateTransition(Duration.seconds(duration), cubeGroup);
        spin.setAxis(Rotate.Y_AXIS);
        spin.setByAngle(180);

        System.out.println(cardCount);
        System.out.println(cardCount);
        PauseTransition halfway = new PauseTransition(Duration.seconds(duration / 4));
        if (cardShowsAnswer) { //when odd, show back of card/ blank side
            halfway.setOnFinished(e -> test.setText(""));
            cardShowsAnswer = false;
        } else { //if front of card / answer side
            halfway.setOnFinished(e -> test.setText(flashcards.get(cardCount).getAnswer()));
            cardShowsAnswer = true;
        }


        new ParallelTransition(spin, halfway).play();


    }


    // sends user response to llama to judge
    @FXML
    private void onSubmit() {
        String response = answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Please enter valid answer");
            return;
        }

        String question = flashcards.get(cardCount).getQuestion();
        String answerInput = flashcards.get(cardCount).getAnswer();

        // Async makes it so rest of the program can still run, while data is being procced through llama
        CompletableFuture.runAsync(() -> {
            try {
                // build HTTP request
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                // Construct the prompt, that says to return llama response in JSON format
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
                //Build the JSON body for Ollama
                // change num_thread to how many cores your cpu has.
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

                //Writes the body to llama
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                // Read the HTTP response
                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                String resp = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines().reduce("", (a, b) -> a + b);

                // Ollama returns something like:
                // {"model":"...","created_at":"...","response":"{\"score\": 2}","done":true,...}
                if (code >= 200 && code < 300) {
                    JSONObject outer = new JSONObject(resp);
                    String responseText = outer.getString("response"); // this is the JSON string
                    int score = new JSONObject(responseText).getInt("score");
                    String grade;
                    if (pvpActive){
                        submitButton.setDisable(true);
                        flipCard();
                        if (p1Turn){
                            p1Score += score;
                            player1.setText("player 1: " + p1Score);

                        } else{
                            p2Score += score;
                            player2.setText("player 2: " + p2Score);

                        }
                        if (!checkWinner()){
                            switchTurns();
                        }

                    }
                    if (score == 1) {
                        grade = " Not close to answer at all";
                    } else if (score == 2 || score == 3) { // note: include 2
                        grade = "Getting there!";
                    } else if (score == 4) { // note: include 2
                        grade = "Almost there!";
                    } else if (score == 5 ) { // cap upper bound
                        grade = " Comparable to answer, good job!";
                    } else {
                        grade = " (score out of range)";
                    }

                    Platform.runLater(() -> aiResponse.setText("Score " + String.valueOf(score) + "/5 " + grade));
                } else {
                    Platform.runLater(() -> aiResponse.setText("Error: " + resp));
                }
                conn.disconnect();

            } catch (Exception e) {
                Platform.runLater(() -> aiResponse.setText("Error: " + e.getMessage()));
            }
        });

    }

    @FXML
    Button modifyButton;

    // opens the edit window for the courses flashcards
    @FXML
    private void onModifyFlashcard() throws IOException {
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createFlashcard-view.fxml"));
        Parent root = fxmlLoader.load();                 // must load before getController()
        CreateFlashcardController b = fxmlLoader.getController();
        b.setCourse(course);
        Scene scene = new Scene(root, HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    // When back button is pressed, go back to main menu
    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }


}



