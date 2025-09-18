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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Model.IFlashcardDAO;
import com.example.projectcab302.Model.SqliteFlashcardDAO;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class FlashcardController {
    @FXML
    public HBox previewOptions;
    @FXML
    public HBox pvpOptions;
    @FXML
    public Button next;
    @FXML
    public Button prev;

    @FXML

    private IFlashcardDAO flashcardDAO;


    @FXML
    private StackPane cubeGroup;
    @FXML
    private Text question;

    private List<Flashcard> flashcards;

    @FXML
    private Text test;
    @FXML
    private Text cardNum;


    // keeps track of card if flipped or not, if clicks is even, then answer is not showing
    private boolean cardShowsAnswer = false;


    // keeps track of which flashcard is currently showing
    private int cardCount = 0;

    @FXML
    private Button pvpSubmitButton;

    @FXML
    private Button ready;
    @FXML
    private Text player1;

    @FXML
    private Text player2;


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

    private Course course;
    public void setCourse(Course Course) {
        cubeGroup.setRotationAxis(Rotate.Y_AXIS);
        cubeGroup.setRotate(180); // rotate 45° before showing

        this.course = Course;
        flashcards = course.getFlashcards();
        question.setText(flashcards.get(cardCount).getQuestion());

        cardNum.setText((cardCount + 1) + "/" + flashcards.size());


    }

    @FXML private ProgressBar bar;
    private Timeline fill; // keep a strong reference

    @FXML private Button flipButton;
    @FXML private Text countdown;
    @FXML private Boolean pvpActive;

    @FXML private Boolean p1Turn;
    @FXML private Boolean p2Turn;

    @FXML private int p1Score;
    @FXML private int p2Score;


    @FXML
    public void onPvP() {
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

        // once user submits save score and flip card


        // give laptop to player 2


        // Enable ready button
        // start 3 second count down and hide question
        // start 10 second progress bar
        // once user submits save score and flip card

        // once all cards are down show who won




        System.out.println("onPvP clicked");


    }

    @FXML
    public void startRound() {
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

    @FXML
    public void onPvpSubmit() {


        if (fill != null && fill.getStatus() == Animation.Status.RUNNING) {
            fill.stop();                 // onFinished WILL NOT fire
        }
        String response = Answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Score 0/5, no answer given" );
            flipCard();
            switchTurns();
        } else{
            onSubmit();
        }


        ready.setDisable(false);
        pvpSubmitButton.setDisable(true);


        Answer.setText("");

    }

    public boolean checkWinner() {
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

    public void switchTurns() {
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
    public void onReady() {
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
            String response = Answer.getText();
            onPvpSubmit();

        });

        fill.playFromStart();
    }

    @FXML
    protected void prevCard() {
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
    protected void nextCard() {

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

    @FXML
    TextArea Answer;
    @FXML
    Text aiResponse;

    @FXML
    Button submitButton;

    @FXML
    private void onSubmit() {
        String response = Answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Please enter valid answer");
            return;
        }

        String question = flashcards.get(cardCount).getQuestion();
        String answer = flashcards.get(cardCount).getAnswer();

        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("http://localhost:11434/api/generate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);

                String prompt =
                        "Return only valid JSON exactly of the form {\"score\": N} where N is an integer 1-5. " +
                                "No extra text, no code fences.\n\n" +
                                "Score Answer B relative to Answer A using this rubric:\n" +
                                "5: Equal in topics and has either an equal or more word count.\n" +
                                "4: Close to Answer A, but lacks in either word count or topics\n" +
                                "3: Lacks in word count and topics, in comparison to answer A\n" +
                                "2: Only include one topic similar to answer A\n" +
                                "1: clearly inferior or incorrect.\n\n" +
                                "Question:" + question + "\n" +
                                "Answer A:" + answer + "\n" +
                                "Answer B:" + response + "\n" +
                                "Output:";
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

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

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
                    } else if (score >= 2 && score <= 4) { // note: include 2
                        grade = "Shares similar topics to answer A";
                    } else if (score == 5 ) { // cap upper bound
                        grade = " Comparable to answer, good job!";
                    } else {
                        grade = " (score out of range)";
                    }

                    Platform.runLater(() -> aiResponse.setText("Score " + String.valueOf(score) + "/5" + grade));
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

    @FXML
    private void onModifyFlashcard() throws IOException {
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createFlashcard-view.fxml"));
        Parent root = fxmlLoader.load();                 // must load before getController()
        CreateFlashcardController b = fxmlLoader.getController();
        b.setCourse(course);
        // pass whatever you need
        Scene scene = new Scene(root, HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    private void onBack() throws IOException {
        Stage stage = (Stage) modifyButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }


}



