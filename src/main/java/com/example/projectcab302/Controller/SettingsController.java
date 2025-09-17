package com.example.projectcab302.Controller;

import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.util.prefs.Preferences;
import com.example.projectcab302.AppSettings;  // <— import from the base package


public class SettingsController {

    @FXML private CheckBox darkModeCheckBox;
    @FXML private Slider fontScaleSlider;
    @FXML private Label  scaleValueLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private CheckBox soundEffectsCheckBox;
    @FXML private CheckBox notificationsCheckBox;

    private final AppSettings settings = AppSettings.getInstance();

    @FXML
    public void initialize() {
        // Populate language choices
        languageComboBox.getItems().setAll("en", "vi", "ja", "ko", "zh");

        // Load from prefs
        darkModeCheckBox.setSelected(settings.isDarkMode());
        fontScaleSlider.setValue(settings.getUiScale());
        languageComboBox.getSelectionModel().select(settings.getLanguage());
        soundEffectsCheckBox.setSelected(settings.isSoundEffects());
        notificationsCheckBox.setSelected(settings.isNotifications());

        // Show scale %
        scaleValueLabel.setText(asPercent(settings.getUiScale()));

        // Live preview listeners
        darkModeCheckBox.selectedProperty().addListener((o, oldV, v) -> {
            settings.setDarkMode(v);
            applyToCurrentScene();
        });

        fontScaleSlider.valueProperty().addListener((o, oldV, v) -> {
            scaleValueLabel.setText(asPercent(v.doubleValue()));
            settings.setUiScale(v.doubleValue());
            applyToCurrentScene();
        });

        languageComboBox.valueProperty().addListener((o, oldV, v) -> {
            if (v != null) settings.setLanguage(v);
            // TODO: you can trigger live i18n refresh here if you use ResourceBundles
        });

        soundEffectsCheckBox.selectedProperty().addListener((o, oldV, v) -> settings.setSoundEffects(v));
        notificationsCheckBox.selectedProperty().addListener((o, oldV, v) -> settings.setNotifications(v));
    }

    private String asPercent(double scale) {
        return Math.round(scale * 100) + "%";
    }

    private void applyToCurrentScene() {
        if (scaleValueLabel == null) return;
        Scene scene = scaleValueLabel.getScene();
        if (scene != null) settings.applyToScene(scene);
    }

    @FXML
    private void handleReset() {
        settings.resetToDefaults();
        // Reflect defaults in UI
        darkModeCheckBox.setSelected(settings.isDarkMode());
        fontScaleSlider.setValue(settings.getUiScale());
        languageComboBox.getSelectionModel().select(settings.getLanguage());
        soundEffectsCheckBox.setSelected(settings.isSoundEffects());
        notificationsCheckBox.setSelected(settings.isNotifications());
        applyToCurrentScene();
    }

    @FXML
    private void handleSave() {
        // Everything already persisted on change; this is just a friendly button
        Alert ok = new Alert(Alert.AlertType.INFORMATION, "Settings saved.");
        ok.showAndWait();
    }

    @FXML
    private void handleBack() {
        ViewManager.getInstance().goBack();
    }
}
