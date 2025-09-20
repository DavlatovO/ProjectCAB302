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
    private int id;


    private String title;
    private static String transferredTitle;

    private ICoursesDAO courseDAO = new SqliteCoursesDAO();



    private IFlashcardDAO flashcardDAO = new SqliteFlashcardDAO();
    private List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();



    private Course course;






    public Course(String title) {

        this.setTitle(title);
    }



    public List<Flashcard> getFlashcards() {

        List<Flashcard> courseCards = new ArrayList<>();

        for (Flashcard card: flashcards){

            if (Objects.equals(card.getCourse(), this.title)){

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
        if (courseDAO.checkTitleExists(this.getTitle())){

            return;
        }

        this.title = modelUtils.checkValidityAndTrim(title, "Course Title");


    }

    public static String getTransferredTitle() {
        return transferredTitle;
    }

    public static void setTransferredTitle(String text) {
        transferredTitle = text;
    }



}