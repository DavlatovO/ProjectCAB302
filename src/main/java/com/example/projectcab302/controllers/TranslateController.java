package com.example.projectcab302;



import com.google.auth.oauth2.GoogleCredentials;


import java.io.FileInputStream;
import java.net.http.*; import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import javafx.fxml.FXML;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;

import javafx.scene.control.*;


import java.util.List;
import java.util.concurrent.CompletableFuture;




import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class TranslateController {
    @FXML
    public TextArea EnglishText;

    @FXML
    public Button translateButton;

    @FXML
    public TextArea VietText;

    @FXML private RadioButton radioOne;
    @FXML private TextField optionOne;

    @FXML private RadioButton radioTwo;
    @FXML private TextField optionTwo;

    @FXML private RadioButton radioThree;
    @FXML private TextField optionThree;

    @FXML private RadioButton radioFour;
    @FXML private TextField optionFour;

    @FXML private RadioButton transRadioOne;
    @FXML private TextField transOptionOne;

    @FXML private RadioButton transRadioTwo;
    @FXML private TextField transOptionTwo;

    @FXML private RadioButton transRadioThree;
    @FXML private TextField transOptionThree;

    @FXML private RadioButton transRadioFour;
    @FXML private TextField transOptionFour;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to the Address Book Application!");
    }

    @FXML
    private CheckBox agreeCheckBox;
    @FXML
    private Button nextButton;


    // Set and pass variables to overloaded translateText() method for translation.

    @FXML
    public void translateText() {
        translateButton.setDisable(true);
        welcomeText.setText("Translating...");

        final String projectId = "mythical-patrol-471101-q1";
        final String target = "vi";

        // 1) Collect inputs in order (EnglishText first, then options)
        List<String> inputs = new ArrayList<>();
        if (!EnglishText.getText().isBlank()) inputs.add(EnglishText.getText());
        if (!optionOne.getText().isBlank())  inputs.add(optionOne.getText());
        if (!optionTwo.getText().isBlank())  inputs.add(optionTwo.getText());
        if (!optionThree.getText().isBlank())inputs.add(optionThree.getText());
        if (!optionFour.getText().isBlank()) inputs.add(optionFour.getText());

        CompletableFuture.supplyAsync(() -> {
            try {
                // 2) OAuth token
                // 2) Load service account key from a JSON file in your folder
                GoogleCredentials creds = GoogleCredentials.getApplicationDefault()
                        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-translation"));
                creds.refreshIfExpired();
                String token = creds.getAccessToken().getTokenValue();


                // 3) Build request JSON
                JsonObject body = new JsonObject();
                JsonArray contents = new JsonArray();
                for (String s : inputs) contents.add(s);
                body.add("contents", contents);
                body.addProperty("mimeType", "text/plain");
                body.addProperty("targetLanguageCode", target);

                // 4) HTTP request
                String url = "https://translation.googleapis.com/v3/projects/" + projectId + "/locations/global:translateText";
                HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                        .build();

                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (resp.statusCode() / 100 != 2) {
                    throw new RuntimeException("HTTP " + resp.statusCode() + ": " + resp.body());
                }

                // 5) Parse response -> list of translated strings
                JsonObject root = JsonParser.parseString(resp.body()).getAsJsonObject();
                JsonArray translations = root.getAsJsonArray("translations");

                List<String> out = new ArrayList<>(translations.size());
                for (int i = 0; i < translations.size(); i++) {
                    out.add(translations.get(i).getAsJsonObject().get("translatedText").getAsString());
                }
                return out;
            } catch (Exception e) {
                return List.of("ERROR: " + e.getMessage());
            }
        }).thenAccept(results -> Platform.runLater(() -> {
            try {
                translateButton.setDisable(false);

                // 6) Error?
                if (results.size() == 1 && results.get(0).startsWith("ERROR:")) {
                    welcomeText.setText(results.get(0));
                    return;
                }

                // 7) Map outputs back in the same order as inputs:
                //    EnglishText -> VietText, then optionOne..Four accordingly
                List<TextInputControl> outputs = List.of(VietText, transOptionOne, transOptionTwo, transOptionThree, transOptionFour);

                for (int i = 0; i < results.size() && i < outputs.size(); i++) {
                    outputs.get(i).setText(results.get(i));
                }

                welcomeText.setText("Done ✔");
            } catch (Exception ex) {
                welcomeText.setText("ERROR (UI): " + ex.getMessage());
            }
        }));
    }

    private static String json(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }



    @FXML
    protected void onAgreeCheckBoxClick() {
        boolean accepted = agreeCheckBox.isSelected();
        nextButton.setDisable(!accepted);
    }



}
