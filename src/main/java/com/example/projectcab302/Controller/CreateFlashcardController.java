package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for creating and editing flashcards for a specific course.
 * <p>
 * This controller:
 * <ul>
 *     <li>Displays existing flashcards for the selected course.</li>
 *     <li>Allows the user to edit flashcards in a text-based format
 *         (<code>question --&#123;answer&#125;</code> per line).</li>
 *     <li>Parses the edited flashcards and saves them to the database.</li>
 *     <li>Enables navigation back to the courses view or forward to a preview view.</li>
 * </ul>
 * </p>
 */
public class CreateFlashcardController extends BaseCourseAndSession {

    // ───────────── Buttons ─────────────

    /**
     * Button that navigates back to the courses view.
     */
    @FXML
    private Button backButton;

    // ───────────── UI Elements ─────────────

    /**
     * Text area where flashcards are displayed and edited.
     * <p>
     * Each line follows the format:
     * <pre>
     *     Question --{Answer}
     * </pre>
     * </p>
     */
    @FXML
    private TextArea cardEntry;

    /**
     * Label that displays the title of the currently selected course.
     */
    @FXML
    private Label titleLabel;

    // ─────── Controller State (Non-UI) ───────

    /**
     * DAO for performing CRUD operations on flashcards.
     */
    private IFlashcardDAO flashcardDAO;

    /**
     * DAO for interacting with course data.
     * (Currently unused in this controller, but available for future use.)
     */
    private ICoursesDAO coursesDAO;


    /**
     * Called automatically after the course is set in the base class.
     * <p>
     * This method populates the text area with the flashcards
     * of the currently selected course.
     * </p>
     */
    @Override
    public void afterCourseisSet() {
        fillTextArea();
    }

    /**
     * Populates the {@link #cardEntry} text area with the flashcards
     * belonging to the current course.
     * <p>
     * If no flashcards exist, sample data is inserted (primarily for testing).
     * Each flashcard is displayed in the format:
     * <pre>
     *     Question --{Answer}
     * </pre>
     * </p>
     */
    private void fillTextArea() {
        titleLabel.setText(course.getTitle());
        List<Flashcard> flashcards = course.getFlashcards();

        // For testing: insert sample data if none exist
        if (flashcards.isEmpty()) {
            flashcardDAO = new SqliteFlashcardDAO();
            flashcardDAO.insertSampleData();
            flashcards = course.getFlashcards();
        }

        for (Flashcard card : flashcards) {
            cardEntry.appendText(card.getQuestion() + "--{" + card.getAnswer() + "}\n");
        }
    }

    /**
     * Saves the flashcards currently entered in the text area to the database.
     * <p>
     * This method:
     * <ul>
     *     <li>Clears all existing flashcards in the database.</li>
     *     <li>Parses the text area content using the pattern
     *         <code>(.*?)--&#123;(.*?)&#125;</code>.</li>
     *     <li>Creates and inserts each flashcard back into the database
     *         under the current course.</li>
     * </ul>
     * </p>
     *
     * @throws IOException if an I/O error occurs during the save operation
     */
    @FXML
    private void onSave() throws IOException {
        flashcardDAO = new SqliteFlashcardDAO();
        flashcardDAO.clearData();

        String text = cardEntry.getText();

        // Pattern to match "Question --{Answer}"
        Pattern pattern = Pattern.compile("(.*?)--\\{(.*?)\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        // Parse and save each flashcard
        while (matcher.find()) {
            String question = matcher.group(1).trim();
            String answer = matcher.group(2).trim();

            Flashcard card = new Flashcard(user, course, question, answer);
            flashcardDAO.addFlashcard(card);
        }
    }

    /**
     * Navigates to the preview page for the flashcards of the current course.
     *
     * @throws IOException if loading the preview view fails
     */
    @FXML
    private void onPreview() throws IOException {
        SceneManager.switchTo("flashcard-view.fxml", this.user, course);
    }

    /**
     * Navigates back to the courses view.
     *
     * @throws IOException if loading the courses view fails
     */
    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("courses-view.fxml", this.user);
    }
}
