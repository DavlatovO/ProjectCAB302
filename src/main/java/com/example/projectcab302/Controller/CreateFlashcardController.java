package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import com.example.projectcab302.SceneManager;
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

public class CreateFlashcardController extends BaseCourseAndSession {

    @FXML
    private TextArea cardEntry;

    @FXML
    private Label titleLabel;


    @FXML
    Button backButton;

    private IFlashcardDAO flashcardDAO;
    private ICoursesDAO coursesDAO;

    @FXML
    private void initialize() {

    }



    // Used for passing what course's flashcards should be seen here
    @Override
    public void afterCourseisSet() {
        fillTextArea();
    }

    private void fillTextArea() {
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


    // When save is pressed, save changes made to flashcards
    @FXML
    private void onSave() throws IOException {
        flashcardDAO = new SqliteFlashcardDAO();
        flashcardDAO.clearData();
        String text = cardEntry.getText();


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


    // When preview is pressed, go to preview page of those flashcards
    @FXML
    private void onPreview() throws IOException {
        SceneManager.switchTo("flashcard-view.fxml", this.user, course);


    }

    // When back button is pressed, go back to main menu
    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("courses-view.fxml", this.user);
    }


}
