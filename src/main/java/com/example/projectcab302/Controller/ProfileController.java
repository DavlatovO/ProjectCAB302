package com.example.projectcab302.Controller;

import com.example.projectcab302.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Objects;

public class ProfileController {

    @FXML private Label titleLabel;

    // Avatar + basic info
    @FXML private ImageView avatarImage;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private Label emailLabel;

    // Editable fields (hidden until Edit)
    @FXML private TextField nameField;
    @FXML private TextField emailField;

    @FXML private Button editSaveButton;
    @FXML private Button uploadAvatarButton;
    @FXML private Button changePasswordButton;
    @FXML private Button backButton;
    @FXML private Button logoutButton;

    private boolean editing = false;

    // Simple state (replace with your actual User model)
    private String currentName = "Student Name";
    private String currentEmail = "student@example.com";
    private String currentRole = "Student";

    @FXML
    private void initialize() {
        titleLabel.setText("Profile");
        // Default avatar (optional)
        try {
            Image placeholder = new Image(
                    Objects.requireNonNull(getClass().getResource("/com/example/projectcab302/avatar-placeholder.png")).toExternalForm(),
                    120, 120, true, true
            );
            avatarImage.setImage(placeholder);
        } catch (Exception ignored) { /* if you don't have an image */ }

        // Load current state into labels
        refreshReadOnly();
        setEditMode(false);
    }

    /** Optionally call this right after loading the FXML to inject real user data. */
    public void setProfileData(String name, String email, String role) {
        if (name != null) currentName = name;
        if (email != null) currentEmail = email;
        if (role != null) currentRole = role;
        refreshReadOnly();
        setEditMode(false);
    }

    private void refreshReadOnly() {
        nameLabel.setText(currentName);
        emailLabel.setText(currentEmail);
        roleLabel.setText(currentRole);

        nameField.setText(currentName);
        emailField.setText(currentEmail);
    }

    private void setEditMode(boolean value) {
        editing = value;
        nameField.setVisible(editing);
        emailField.setVisible(editing);

        nameLabel.setVisible(!editing);
        emailLabel.setVisible(!editing);

        editSaveButton.setText(editing ? "Save" : "Edit");
        uploadAvatarButton.setDisable(editing); // keep media actions separate while editing
        changePasswordButton.setDisable(editing);
    }

    @FXML
    private void handleEditSave(ActionEvent e) {
        if (!editing) {
            setEditMode(true);
        } else {
            // TODO: persist to DB here
            currentName = nameField.getText().trim();
            currentEmail = emailField.getText().trim();
            refreshReadOnly();
            setEditMode(false);
            System.out.println("Profile saved: " + currentName + ", " + currentEmail);
        }
    }

    @FXML
    private void handleUploadAvatar(ActionEvent e) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose avatar image");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File file = chooser.showOpenDialog(avatarImage.getScene().getWindow());
        if (file != null) {
            avatarImage.setImage(new Image(file.toURI().toString(), 120, 120, true, true));
        }
    }

    @FXML
    private void handleChangePassword(ActionEvent e) {
        // Minimal UX; replace with your dialog or dedicated view
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new password");
        dialog.setContentText("New password:");
        dialog.showAndWait().ifPresent(pw -> {
            // TODO: validate + persist
            System.out.println("Password changed (demo): " + pw.replaceAll(".", "*"));
        });
    }

    @FXML
    private void handleBack(ActionEvent e) {
        // If embedded in a BorderPane center, controllers can swap back to dashboard view
        // or close the window if opened in a separate Stage. No-op here.
        ViewManager.getInstance().goBack();
    }

    @FXML
    private void handleLogout(ActionEvent e) {
        System.out.println("Logout requested from Profile page");
        // TODO: route back to login scene
    }
}
