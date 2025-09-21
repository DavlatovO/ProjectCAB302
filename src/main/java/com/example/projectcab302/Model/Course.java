package com.example.projectcab302.Model;

import com.example.projectcab302.Persistence.ICoursesDAO;
import com.example.projectcab302.Persistence.IFlashcardDAO;
import com.example.projectcab302.Persistence.SqliteCoursesDAO;
import com.example.projectcab302.Persistence.SqliteFlashcardDAO;
import com.example.projectcab302.modelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {

    // Unique identifier for the course (matches DB ID)
    private int id;

    // Title of the course (e.g., "CAB202")
    private String title;

    // Used for transferring course titles across classes (static shared variable)
    private static String transferredTitle;

    // DAO objects for database access
    private ICoursesDAO courseDAO = new SqliteCoursesDAO();
    private IFlashcardDAO flashcardDAO = new SqliteFlashcardDAO();

    // List of all flashcards from the DB
    private List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();

    // Unused self-reference (potentially for future features)
    private Course course;

    // Constructor – requires a course title
    public Course(String title) {
        this.setTitle(title);
    }

    // Returns only the flashcards that belong to this course
    public List<Flashcard> getFlashcards() {
        List<Flashcard> courseCards = new ArrayList<>();
        for (Flashcard card : flashcards) {
            if (Objects.equals(card.getCourse(), this.title)) {
                courseCards.add(card);
            }
        }
        return courseCards;
    }

    // Getters and setters for ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Getter for course title
    public String getTitle() {
        return this.title;
    }

    // Setter for course title – validates and trims input before saving
    public void setTitle(String title) {
        this.title = modelUtils.checkValidityAndTrim(title, "Course Title");
    }
}
