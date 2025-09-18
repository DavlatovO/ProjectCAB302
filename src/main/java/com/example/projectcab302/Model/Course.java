package com.example.projectcab302.Model;

import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private int id;
    private List<Flashcard> flashcards;
    private String title;
    private static String transferredTitle;
    private ICoursesDAO courseDAO;
    private List<Course> courses;

    private Course course;




    public Course(String title) {

        setTitle(title);
    }

    private IFlashcardDAO flashcardDAO;

    public List<Flashcard> getFlashcards() {

        flashcardDAO = new SqliteFlashcardDAO();
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();

        List<Flashcard> courseCards = new ArrayList<>();
        for (Flashcard card: flashcards){
            System.out.println(this.title);
            System.out.println(card.getCourse());
            if (Objects.equals(card.getCourse(), this.title)){
                System.out.println("yes");
                courseCards.add(card);
            }
        }
        return courseCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {

        return this.title;
    }

    public void setTitle(String title) {
        courseDAO = new SqliteCoursesDAO();
        courses = courseDAO.getAllCourses();

        for (Course c: courses){
            if (c.getTitle() == title){
                throw new IllegalArgumentException("Course already exists");
            }
        }
        this.title = checkValidityAndTrim(title);
    }

    public static String getTransferredTitle() {
        return transferredTitle;
    }

    public static void setTransferredTitle(String text) {
        transferredTitle = text;
    }

    private String checkValidityAndTrim(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or blank");
        }
        return input.trim();
    }

}