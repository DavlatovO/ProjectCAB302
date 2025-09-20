package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.example.projectcab302.HelloApplication;

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
    private ICoursesDAO coursesDAO;

    @FXML
    private void initialize() {
    }

    private Course course;
    public void setCourse(Course Course) {
        this.course = Course;

        titleLabel.setText(course.getTitle());
        List<Flashcard> flashcards = course.getFlashcards();

        //Mainly for testing
        if (flashcards.isEmpty()){
            flashcardDAO = new SqliteFlashcardDAO();
            flashcardDAO.insertSampleData();
            flashcards = course.getFlashcards();
        }

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
            System.out.println(this.course.getTitle());
            System.out.println(question);
            System.out.println(answer);
            flashcardDAO.addFlashcard(card);
        }

    }



    @FXML
    Button backButton;
    @FXML
    protected void onPreview() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("flashcard-view.fxml"));
        Parent root = fxmlLoader.load();                 // must load before getController()
        FlashcardController b = fxmlLoader.getController();
        b.setCourse(course);
        // pass whatever you need
        Scene scene = new Scene(root, HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);

    }

    @FXML
    protected void onBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("courses-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}
