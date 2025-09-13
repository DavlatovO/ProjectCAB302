package com.example.projectcab302.controllers;

import com.example.projectcab302.utils.Database;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.google.auth.oauth2.ServiceAccountCredentials;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranslateController {

    @FXML
    private Label translatedQuestionLabel;

    @FXML
    private Button showTranslatedQuestionBtn;

    private static final String DEFAULT_TARGET_LANGUAGE = "vi"; // Vietnamese

    private Translate translate;
    private Connection connection;

    /** No-arg constructor required by FXMLLoader */
    public TranslateController() {
        // Nothing here
    }

    /** Initialize Cloud Translate and get DB connection */
    public void init(String credentialsPath, String projectId) throws Exception {
        // Initialize Google Cloud Translate
        translate = TranslateOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(credentialsPath)))
                .setProjectId(projectId)
                .build()
                .getService();

        // Get SQLite connection from Database util
        connection = Database.getConnection();
    }

    /** Called automatically after @FXML injection */
    @FXML
    private void initialize() {
        showTranslatedQuestionBtn.setOnAction(event -> {
            if (connection == null) {
                Alert alert = new Alert(AlertType.ERROR, "Database not initialized. Call init() first.");
                alert.showAndWait();
                return;
            }

            try {
                // Example: translate question with ID 1
                String translated = translateQuestion(1, DEFAULT_TARGET_LANGUAGE);
                translatedQuestionLabel.setText(translated);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Database error: " + e.getMessage());
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Translation error: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }

    /** Translate a question by ID using Cloud Translate */
    public String translateQuestion(int questionId, String targetLanguage) throws SQLException {
        String query = "SELECT question_text FROM Question WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String originalText = rs.getString("question_text");

                Translation translation = translate.translate(
                        originalText,
                        Translate.TranslateOption.targetLanguage(targetLanguage)
                );

                return translation.getTranslatedText();
            } else {
                return "Question not found.";
            }
        }
    }

    /** Close DB connection safely */
    public void close() {
        Platform.runLater(() -> {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
