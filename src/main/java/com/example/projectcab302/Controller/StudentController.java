package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Score;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Persistence.IScoresDAO;

import com.example.projectcab302.Persistence.SqliteScoreDAO;

import com.example.projectcab302.Utils.Session;
import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.util.Duration;


/**
 * Controller for the student dashboard view.

 */
public class StudentController extends BaseSession {

    @FXML private VBox dashboard;

    @FXML
    protected void openUserProfile() {
        ViewManager.getInstance().switchToProfileView(
                "Student Name",
                "student@example.com",
                "Student"
        );
    }

    // Displays a progress ring, along with labels indicating type of metric, and percentages
    private void gpaRing(double gpa, String gradeMeasure, String attempts) {
        StackPane stackPane = new StackPane();
        Text label = new Text(String.format("%s%.2f%%", gradeMeasure, gpa * 100) + "\n"+ attempts);

        Student student = (Student) this.user;

        Arc arc = new Arc(100, 100, 90, 90, 90, 360);
        arc.setType(ArcType.OPEN);
        arc.setStroke(Color.DODGERBLUE);
        arc.setStrokeWidth(8);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setFill(Color.TRANSPARENT);


        double radius = 90;
        double circumference = 2 * Math.PI * radius;

        arc.getStrokeDashArray().addAll(circumference);
        arc.setStrokeDashOffset(circumference);

        stackPane.getChildren().add(arc);
        stackPane.getChildren().add(label);
        dashboard.getChildren().add(stackPane);


        Timeline fill = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(arc.strokeDashOffsetProperty(), circumference)),
                new KeyFrame(Duration.seconds(2), new KeyValue(arc.strokeDashOffsetProperty(), circumference - (circumference * gpa)))
        );
        fill.play();

    }

    @FXML
    protected void openSettings() {
        ViewManager.getInstance().switchToSettingsView();
    }

    @FXML
    protected void logout() {
        Session.clear();
        SceneManager.switchTo("landingpage.fxml");
    }

    @FXML
    protected void quiz() {
        SceneManager.switchTo("courses-view.fxml", this.user, "studentQuiz.fxml");
    }

    @FXML
    protected void createFlashcard() {
        SceneManager.switchTo("courses-view.fxml", this.user, "flashcard-view.fxml");
    }

    @FXML
    protected void viewFlashcards() {

    }

    @Override
    public void afterUserisSet() {
        Student student = (Student) this.user;
        IScoresDAO scoresDAO = new SqliteScoreDAO();
        Score score = scoresDAO.getScore(student.getId());
        double quizScore = score.getQuizScore();
        double pvpScore = score.getPvpScore();
        int pvpBattles = score.getPvpBattle();
        int quizAttempts = score.getQuizAttempts();
        String quizText = "Your quiz average is: ";
        String quizType = "with "+ quizAttempts + " quiz submissions";
        gpaRing(quizScore, quizText, quizType);

        String pvpText = "Your PVP winrate is: ";
        String pvpType = "with "+ pvpBattles + " PvP Battles";
        gpaRing(pvpScore, pvpText, pvpType);
    }
}
