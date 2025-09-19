package com.example.projectcab302.Controller;

import com.example.projectcab302.SceneManager;
import com.example.projectcab302.z_Misc.AppSettings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class SettingsController {

    @FXML private CheckBox darkModeCheckBox;
    @FXML private Slider   fontScaleSlider;
    @FXML private Label    scaleValueLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private CheckBox soundEffectsCheckBox;
    @FXML private CheckBox notificationsCheckBox;

    private final AppSettings settings = AppSettings.getInstance();

    private static final String LIGHT = "/com/example/projectcab302/style.css";
    private static final String DARK  = "/com/example/projectcab302/style-dark.css";

    private String cssUrl(String path) {
        var url = getClass().getResource(path);
        return (url == null) ? null : url.toExternalForm();
    }

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

        // Apply current settings to the scene (if attached)
        applyToCurrentScene();

        // Live preview: dark mode
        darkModeCheckBox.selectedProperty().addListener((o, oldV, v) -> {
            settings.setDarkMode(v);
            applyToCurrentScene();
        });

        // Live preview: scale
        fontScaleSlider.valueProperty().addListener((o, oldV, v) -> {
            double val = v.doubleValue();
            settings.setUiScale(val);
            scaleValueLabel.setText(asPercent(val));
            applyToCurrentScene();
        });

        // Persist language/sfx/noti
        languageComboBox.valueProperty().addListener((o, oldV, v) -> { if (v != null) settings.setLanguage(v); });
        soundEffectsCheckBox.selectedProperty().addListener((o, oldV, v) -> settings.setSoundEffects(v));
        notificationsCheckBox.selectedProperty().addListener((o, oldV, v) -> settings.setNotifications(v));
    }

    private String asPercent(double scale) {
        return Math.round(scale * 100) + "%";
    }

    /** Attach exactly one theme CSS (light or dark), then let AppSettings apply scale/classes. */
    private void applyToCurrentScene() {
        if (scaleValueLabel == null) return;
        Scene scene = scaleValueLabel.getScene();
        if (scene == null) return;

        String css = cssUrl(settings.isDarkMode() ? DARK : LIGHT);
        scene.getStylesheets().clear();
        if (css != null) scene.getStylesheets().add(css);

        settings.applyToScene(scene);  // scale + root classes
    }

    @FXML
    private void handleReset() {
        settings.resetToDefaults();
        darkModeCheckBox.setSelected(settings.isDarkMode());
        fontScaleSlider.setValue(settings.getUiScale());
        languageComboBox.getSelectionModel().select(settings.getLanguage());
        soundEffectsCheckBox.setSelected(settings.isSoundEffects());
        notificationsCheckBox.setSelected(settings.isNotifications());
        applyToCurrentScene();
    }

    @FXML
    private void handleSave() {
        new Alert(Alert.AlertType.INFORMATION, "Settings saved.").showAndWait();
    }

    @FXML
    private void handleBack() {
        SceneManager.goBack();
    }

    // If wired in FXML, this simply mirrors the checkbox listener.
    @FXML
    private void onDarkModeToggled() {
        settings.setDarkMode(darkModeCheckBox.isSelected());
        applyToCurrentScene();
    }
}