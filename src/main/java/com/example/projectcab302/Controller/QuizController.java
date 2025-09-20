package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Quiz;
import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.PopupWindow;

import java.io.IOException;
import java.util.List;


public class QuizController {
    public Button submitButton;
    public Label errorQuizLabel;
    public TextField answerField;
    public TextField optionDField;
    public TextField optionCField;
    public TextField optionBField;
    public TextField optionAField;
    public TextField questionField;
    public ListView questionListView;
    @FXML
    private VBox SelectedEdit;
    @FXML
    private GridPane courseQuizGrid;
    @FXML
    private VBox allCourses;
    @FXML
    private ICoursesDAO courseDAO;
    @FXML
    private List<Course> courses;
    @FXML
    private Course course;
    protected void saveQuiz() {

    }

    @FXML
    private void initialize(){
        //SelectedEdit.managedProperty().bind(SelectedEdit.visibleProperty());
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();
        System.out.println(allCourses.heightProperty());
        VBox.setVgrow(allCourses, Priority.SOMETIMES);
        if (courses.isEmpty()) {
            courseDAO.insertSampleData();
            courses = courseDAO.getAllCourses();
        }
    
        // tighter spacing & padding on the VBox
            allCourses.setSpacing(6);                  // was 12 in FXML
            allCourses.setPadding(new Insets(6));      // override "-fx-padding: 12" if needed
    
        HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);
            row.setSpacing(8);
            row.setMaxWidth(Double.MAX_VALUE);         // let row stretch with the VBox
            //VBox.setVgrow(row, Priority.NEVER);     // default; don't expand rows vertically
    
            for (int i = 0; i < courses.size(); i++) {
            final int idx = i;
            Button btn = new Button(courses.get(idx).getTitle());
    
            btn.setOnAction(e -> {
                try {
                    course = courses.get(idx);
                    //Course.setTransferredTitle(course.getTitle());
                    onCourse();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
    
            btn.setMinWidth(0);
            btn.setMaxWidth(Double.MAX_VALUE);
            GridPane.setFillWidth(btn, true);
            GridPane.setHgrow(btn, Priority.ALWAYS);
    
            int col = i % 3;
            int rowss = i / 3;
            GridPane.setColumnIndex(btn, col);
            GridPane.setRowIndex(btn, rowss);
    
            courseQuizGrid.getChildren().add(btn);

        }
    }
    @FXML
    private void onCourse() throws IOException{
        //allCourses.setManaged(false);
        //allCourses.setVisible(false);
        SceneManager.switchTo("Edit-Quiz");

    }

    private void selectCourse(Quiz question) {
        questionListView.getSelectionModel().select(question);
         = course.getTitle();
        quizQuestion = question.getQuizQuestion();
        optionAField.
        System.out.println(courseName);
    }

    /**
     * Renders a cell in the contacts list view by setting the text to the contact's full name.
     * @param courseListView The list view to render the cell for.
     * @return The rendered cell.
     */
    private ListCell<Course> renderCell(ListView<Course> courseListView) {
        return new ListCell<>() {
            /**
             * Handles the event when a contact is selected in the list view.
             * @param mouseEvent The event to handle.
             */

            private void onCourseSelected(MouseEvent mouseEvent) {
                ListCell<Course> clickedCell = (ListCell<Course>) mouseEvent.getSource();
                // Get the selected contact from the list view
                Course selectedCourse = clickedCell.getItem();
                if (selectedCourse != null) selectCourse(selectedCourse);
            }

            /**
             * Updates the item in the cell by setting the text to the contact's full name.
             * @param course The contact to update the cell with.
             * @param empty Whether the cell is empty.
             */
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                // If the cell is empty, set the text to null, otherwise set it to the contact's full name
                if (empty || course == null || course.getTitle() == null) {
                    setText(null);
                    super.setOnMouseClicked(this::onCourseSelected);
                } else {
                    setText(course.getTitle());
                }
            }
        };

    }

    /**
     * Synchronizes the contacts list view with the contacts in the database.
     */
    private void syncCourse() {
        coursesDAO = new SqliteCoursesDAO();

        courseListView.getItems().clear();
        List<Course> courses = coursesDAO.getAllCourses();
        //boolean hasCourse = !courses.isEmpty();
        courseListView.getItems().addAll(courses);

        // Show / hide based on whether there are contacts
        //ontactContainer.setVisible(hasContact);
    }

    @FXML
    public void initialize() {
        courseListView.setCellFactory(this::renderCell);
        syncCourse();
        // Select the first contact and display its information
        courseListView.getSelectionModel().selectFirst();
        Course course = courseListView.getSelectionModel().getSelectedItem();
        if (course != null) {
            selectCourse(course);
        }
    }



    @FXML
    protected void onBack() {
        SceneManager.switchTo("teacher-view.fxml"); //Need to edit later to make functional with student view
    }

    @FXML
    protected void createQuiz() {
        SceneManager.switchTo("create-quiz.fxml");
    }
    //Note bug where if create quiz is pressed twice the scene manager switches to Teacher-view.fxml


    public void editQuiz(ActionEvent actionEvent) {
    }

    public void deleteQuiz(ActionEvent actionEvent) {
    }

    public void submitQuiz(ActionEvent actionEvent) {
    }
}
