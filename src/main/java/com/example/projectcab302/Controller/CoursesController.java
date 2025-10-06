package com.example.projectcab302.Controller;


import com.example.projectcab302.Model.*;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IUserDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.Persistence.SqliteUserDAO;
import com.example.projectcab302.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.List;


/**
 * Controller for the Courses view.

 */
public class CoursesController extends BaseFXMLandSession{
    // ───────────── Layouts ─────────────
    @FXML private VBox allCourses;
    @FXML private GridPane courseGrid;
    @FXML private HBox courseBox;

    // ───────────── Buttons ─────────────
    @FXML Button createCourse;
    @FXML Button back;

    // ───────────── Texts ─────────────
    @FXML private TextField courseField;

    // ─────── Controller state (non-UI, no @FXML) ───────
    private ICoursesDAO courseDAO;
    private List<Course> courses;
    private Course course;



    /**
     * Initializes the controller.
     * <p>
     * This method is automatically called after the FXML view is loaded.
     * It initializes the {@link ICoursesDAO}, retrieves courses from the database,
     * inserts sample data if the database is empty, and populates the courses grid.
     * </p>
     */
    @FXML
    private void initialize() {
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();

        // Insert sample data if no courses exist
        if (courses.isEmpty()) {
            courseDAO.insertSampleData();
            courses = courseDAO.getAllCourses();
        }

        createCoursesGrid();
    }




    /**
     * Populates the grid with buttons for all available courses.
     * <p>
     * Each course is displayed as a button. Clicking a button switches
     * the scene to the flashcard creation view for that course.
     * </p>
     */
    private void createCoursesGrid() {
        // Ensure the grid is empty before repopulating (optional improvement)
        courseGrid.getChildren().clear();

        for (int i = 0; i < courses.size(); i++) {
            final int idx = i;

            // Create a button for each course
            Button btn = new Button(courses.get(idx).getTitle());

            // When clicked, switch to the flashcard creation view for this course
            btn.setOnAction(e -> SceneManager.switchTo(this.fxml, this.user, courses.get(idx)));

            // Let button expand in its grid cell
            btn.setMinWidth(0);
            btn.setMaxWidth(Double.MAX_VALUE);
            GridPane.setFillWidth(btn, true);
            GridPane.setHgrow(btn, Priority.ALWAYS);

            // Position button in a 3-column grid
            int col = i % 3;
            int row = i / 3;
            GridPane.setColumnIndex(btn, col);
            GridPane.setRowIndex(btn, row);

            // Add button to the grid
            courseGrid.getChildren().add(btn);
        }
    }

    /**
     * Handles the action when the "Create Course" button is pressed.
     * <p>
     * Reads the course name from the {@link #courseField},
     * validates that it is not empty, creates a new {@link Course},
     * and adds it to the database. Finally, it refreshes the course grid.
     * </p>
     *
     * @throws IOException if loading the view fails
     */
    @FXML
    private void onCreateCourse() throws IOException {
        String courseInput = courseField.getText();

        // Validate input
        if (courseInput.trim().isEmpty()) {
            return;
        }


        // Create and add the new course
        Course newCourse = new Course(courseInput, user);
        courseDAO.addCourse(newCourse);

        // Refresh the course grid
        initialize();
    }

    /**
     * Handles the action when the "Back" button is pressed.
     * <p>
     * Navigates the user back to the main teacher view.
     * </p>
     *
     * @throws IOException if loading the view fails
     */
    @FXML
    private void onBack() throws IOException {
        SceneManager.switchTo("teacher-view.fxml", user);
    }



}
