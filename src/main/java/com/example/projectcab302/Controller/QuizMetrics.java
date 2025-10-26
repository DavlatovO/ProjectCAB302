package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.IQuizDAO;
import com.example.projectcab302.Persistence.SqlQuizDAO;
import com.example.projectcab302.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class QuizMetrics extends BaseCourseAndSession {


    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("teacher-view.fxml", this.user);
    }

    @FXML private TableView<Quiz> quizTable;
    @FXML private TableColumn<Quiz, String> questionColumn;
    @FXML private TableColumn<Quiz, Integer> correctColumn;
    @FXML private TableColumn<Quiz, Integer> wrongColumn;
    @FXML private TableColumn<Quiz, String> averageColumn;
    @FXML private VBox Metrics;

    @Override
    protected void afterCourseisSet() {
        IQuizDAO quizDAO = new SqlQuizDAO();
        List<Quiz> quizzes = quizDAO.getAllQuestionsfromCourse(this.course.getTitle());
        double quizAvg = 0;
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("quizQuestion"));
        correctColumn.setCellValueFactory(new PropertyValueFactory<>("correct"));
        wrongColumn.setCellValueFactory(new PropertyValueFactory<>("wrong"));
        averageColumn.setCellValueFactory(new PropertyValueFactory<>("average"));
        averageColumn.setCellValueFactory(cellData -> {
            Quiz quiz = cellData.getValue();
            double avg = quiz.getAverage();
            return new SimpleStringProperty(String.format("%.1f%%", avg));
        });


        quizTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        quizTable.setEditable(false);
        quizTable.setItems(FXCollections.observableArrayList(quizzes));

        for (Quiz quiz: quizzes){
            quizAvg += quiz.getAverage();
        }
        quizAvg = quizAvg / quizzes.size();

        quizAvg = Math.round(quizAvg * 100.0) / 100.0;

        Text text = new Text("Students scored an average of " + quizAvg + "% on this quiz");
        Metrics.getChildren().add(text);
        System.out.println(quizzes);
    }
}
