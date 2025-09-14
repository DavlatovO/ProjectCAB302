package com.example.projectcab302.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.example.projectcab302.HelloApplication;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Model.IFlashcardDAO;
import com.example.projectcab302.Model.SqliteFlashcardDAO;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateFlashcardController {
    @FXML
    private TextArea cardEntry;

    private IFlashcardDAO flashcardDAO;

    @FXML
    private void initialize() {

        flashcardDAO = new SqliteFlashcardDAO();
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();

        for (Flashcard card : flashcards){
            cardEntry.appendText(card.getQuestion() + "--{" + card.getAnswer() + "}" + "\n");
        }

    }

    @FXML
    protected void onSave() throws IOException {
        flashcardDAO = new SqliteFlashcardDAO();
        flashcardDAO.clearData();
        String text = cardEntry.getText();

        // Regex: (.*?) = question, --{(.*?)} = answer
        // DOTALL makes '.' match across newlines
        Pattern pattern = Pattern.compile("(.*?)--\\{(.*?)\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String question = matcher.group(1).trim();
            String answer   = matcher.group(2).trim();

            Flashcard card = new Flashcard(question, answer);

            flashcardDAO.addFlashcard(card);
        }

    }



    @FXML
    Button backButton;
    @FXML
    protected void onBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("flashcard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
