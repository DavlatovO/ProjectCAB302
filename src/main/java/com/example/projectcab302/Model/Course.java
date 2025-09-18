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

    public Course(String title) {
    this.title = title;
    }

    private IFlashcardDAO flashcardDAO;

    public List<Flashcard> getFlashcards() {

        flashcardDAO = new SqliteFlashcardDAO();
        List<Flashcard> flashcards = flashcardDAO.getAllFlashcard();

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

    public void setTitle(int id) {
        this.title = title;
    }

    public static String getTransferredTitle() {
        return transferredTitle;
    }

    public static void setTransferredTitle(String text) {
        transferredTitle = text;
    }



}
