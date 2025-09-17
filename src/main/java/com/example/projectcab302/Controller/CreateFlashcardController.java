package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

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

    @FXML
    private Label titleLabel;

    private IFlashcardDAO flashcardDAO;

    @FXML
    private void initialize() {
        titleLabel.setText(course.getTransferredTitle()); // retrieve input from CoursesController for course title
    }

    private Course course;
    public void setCourse(Course Course) {
        this.course = Course;
        System.out.println(this.course.getTitle());
        List<Flashcard> flashcards = course.getFlashcards();

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

            Flashcard card = new Flashcard(course.getTitle(), question, answer);

            flashcardDAO.addFlashcard(card);
        }

    }

    @FXML
    Button backButton;
    @FXML
    protected void onPreview() throws IOException {
        SceneManager.switchTo("flashcard-view.fxml");

    }

    @FXML
    protected void onBack() throws IOException {
     SceneManager.switchTo("courses-view.fxml");
    }
}
