package com.example.projectcab302.Controller;

import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
    private Label welcomeText;
    private IFlashcardDAO flashcardDAO;


    @FXML
    private StackPane cubeGroup;
    @FXML
    private Text question;
    @FXML
    private Box cube;
    @FXML
    private VBox frontFace;
    @FXML
    private Text test;
    @FXML
    private Text cardNum;

    @FXML
    private Button flip;

    private int clicks = 0;
    private int cardCount = 0;


    @FXML
    private void initialize() {

        cubeGroup.setRotationAxis(Rotate.Y_AXIS);
        cubeGroup.setRotate(180); // rotate 45° before showing
        flashcardDAO = new SqliteFlashcardDAO();
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
        if (flashcards.isEmpty()){
            flashcardDAO.insertSampleData();
            flashcards = flashcardDAO.getAllFlashcard();
        }
        question.setText(flashcards.get(cardCount).getQuestion());

        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    @FXML
    protected void prevCard() {
        if (cardCount == 0) {
            List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
            cardCount = flashcards.size() - 1;
        } else {
            cardCount--;
        }

        if (clicks % 2 == 1) { //when odd, show back of card/ blank side
            flipCard();
        }
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    @FXML
    protected void nextCard() {
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
        if (cardCount == flashcards.size() - 1) {
            cardCount = 0;
        } else {
            cardCount++;
        }

        if (clicks % 2 == 1) { //when odd, show back of card/ blank side
            flipCard();
        }
        question.setText(flashcards.get(cardCount).getQuestion());
        cardNum.setText((cardCount + 1) + "/" + flashcards.size());
    }

    @FXML
    protected void flipCard() {

        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
        float duration = 0.25F;

        // Make 2D face participate in depth testing
        //cube.setDepthTest(DepthTest.ENABLE);
        //frontFace.setDepthTest(DepthTest.ENABLE);

        // Size the face to match the cube’s front
        //frontFace.setPrefSize(cube.getWidth(), cube.getHeight());

        // Place the face exactly on the front side: translateZ = depth/2 + epsilon
        //frontFace.setTranslateZ(cube.getDepth() / 2.0 + 0.5); // 0.5 avoids z-fighting
        //test.setTranslateZ(cube.getDepth() / 2.0 + 0.5); // 0.5 avoids z-fighting

        // Rotate the whole unit (box + face) together
        RotateTransition spin = new RotateTransition(Duration.seconds(duration), cubeGroup);
        spin.setAxis(Rotate.Y_AXIS);
        spin.setByAngle(180);


        PauseTransition halfway = new PauseTransition(Duration.seconds(duration / 4));
        if (clicks % 2 == 1) { //when odd, show back of card/ blank side
            halfway.setOnFinished(e -> test.setText(""));
        } else { //if front of card / answer side
            halfway.setOnFinished(e -> test.setText(flashcards.get(cardCount).getAnswer()));
        }
        clicks++;


        //spin.setCycleCount(Animation.INDEFINITE);
        new ParallelTransition(spin, halfway).play();


    }

    @FXML
    TextArea Answer;
    @FXML
    Text aiResponse;

    @FXML
    protected void onSubmit() {
        String response = Answer.getText();
        if (response.isEmpty()) {
            aiResponse.setText("Please enter valid answer");
            return;
        }
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();
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
                        "Return only valid JSON exactly of the form {\"score\": N} where N is an integer 1-10. " +
                                "No extra text, no code fences.\n\n" +
                                "Score Answer B relative to Answer A using this rubric:\n" +
                                "10: B strictly superior in accuracy AND completeness.\n" +
                                "8-9: B better on ≥1 criterion and not worse on the other.\n" +
                                "5: roughly equal.\n" +
                                "2-4: worse.\n" +
                                "1: clearly inferior or incorrect.\n\n" +
                                "Question:" + question + "\n" +
                                "Answer A:" + answer + "\n" +
                                "Answer B:" + response + "\n" +
                                "Output:";
                // change num_thread to how many cores your cpu has
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
                    if (score == 1) {
                        grade = " Not close to answer at all";
                    } else if (score >= 2 && score <= 4) { // note: include 2
                        grade = " Somewhat close to answer";
                    } else if (score >= 5 && score <= 10) { // cap upper bound
                        grade = " Comparable to answer, good job!";
                    } else {
                        grade = " (score out of range)";
                    }

                    Platform.runLater(() -> aiResponse.setText("Score " + String.valueOf(score) + "/10" + grade));
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
    Button createButton;

    @FXML
    protected void onCreateFlashcard() throws IOException {
        Stage stage = (Stage) createButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createFlashcard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    @FXML
    protected void onBack() throws IOException {
        Stage stage = (Stage) createButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("teacher-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }


}



