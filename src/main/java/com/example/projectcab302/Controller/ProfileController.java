package com.example.projectcab302.Controller;

import com.example.projectcab302.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;

/**
 * Controller for the user profile view.
 * <p>
 * Handles displaying and editing user details, uploading avatars,
 * and performing profile-related actions such as password changes and logout.
 */
public class ProfileController {

    @FXML private Label  nameLabel;
    @FXML private Label  emailLabel;
    @FXML private Label  roleLabel;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private ImageView avatarImage;
    @FXML private Button editSaveBtn;

    private boolean editing = false;

    private String currentName  = "Student Name";
    private String currentEmail = "student@example.com";
    private String currentRole  = "Student";

    @FXML
    private void initialize() {
        if (avatarImage != null) {
            try {
                URL url = getClass().getResource("/com/example/projectcab302/avatar-placeholder.png");
                if (url != null) avatarImage.setImage(new Image(url.toExternalForm(), 120, 120, true, true));
            } catch (Exception ignored) {}
        }
        refreshView();
        setEditMode(false);
    }

    public void setProfileData(String name, String email, String role) {
        if (name != null) currentName = name;
        if (email != null) currentEmail = email;
        if (role != null) currentRole = role;
        refreshView();
        setEditMode(false);
    }


    @FXML
    protected void back() {
        ViewManager.getInstance().goBack();
    }

    @FXML
    protected void logout() {
        ViewManager.getInstance().goBack();
    }

    @FXML
    protected void uploadAvatar() {
        if (avatarImage == null) return;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose avatar image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File f = chooser.showOpenDialog(avatarImage.getScene().getWindow());
        if (f != null) {
            avatarImage.setImage(new Image(f.toURI().toString(), 120, 120, true, true));
        }
    }

    @FXML
    protected void editSave() {
        if (!editing) {
            setEditMode(true);
        } else {
            // TODO: persist to DB
            if (nameField  != null) currentName  = nameField.getText().trim();
            if (emailField != null) currentEmail = emailField.getText().trim();
            refreshView();
            setEditMode(false);
        }
    }

    /**
     * Opens a dialog for the user to change their password.
     */
    @FXML
    protected void changePassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new password");
        dialog.setContentText("New password:");
        dialog.showAndWait().ifPresent(pw -> {
            // TODO: validate + persist
            System.out.println("Password changed (demo): " + "*".repeat(pw.length()));
        });
    }

    // Toggles between view and edit mode for profile fields
    private void setEditMode(boolean on) {
        editing = on;

        if (nameField != null)  { nameField.setVisible(on);  nameField.setManaged(on); }
        if (emailField != null) { emailField.setVisible(on); emailField.setManaged(on); }

        if (nameLabel != null)  { nameLabel.setVisible(!on);  nameLabel.setManaged(!on); }
        if (emailLabel != null) { emailLabel.setVisible(!on); emailLabel.setManaged(!on); }

        if (editSaveBtn != null) editSaveBtn.setText(on ? "Save" : "Edit");
    }

    // Refreshes profile display with current user data
    private void refreshView() {
        if (nameLabel  != null) nameLabel.setText(currentName);
        if (emailLabel != null) emailLabel.setText(currentEmail);
        if (roleLabel  != null) roleLabel.setText(currentRole);

        if (nameField  != null) nameField.setText(currentName);
        if (emailField != null) emailField.setText(currentEmail);
    }
}
