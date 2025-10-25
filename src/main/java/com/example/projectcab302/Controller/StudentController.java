package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Score;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Persistence.IScoresDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.Persistence.SqliteScoreDAO;

import com.example.projectcab302.Utils.Session;
import com.example.projectcab302.SceneManager;
import com.example.projectcab302.ViewManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;

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


    private void gpaRing(double gpa, String gradeMeasure) {
        StackPane stackPane = new StackPane();
        Text label = new Text(String.format("%s%.2f%%", gradeMeasure, gpa * 100));

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
        String quizText = "Your quiz average is: ";
        gpaRing(quizScore, quizText);
        String pvpText = "Your PVP winrate is: ";
        gpaRing(pvpScore, pvpText);
    }
}
