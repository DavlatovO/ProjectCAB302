package com.example.projectcab302;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Flashcard;
import com.example.projectcab302.Model.Teacher;
import com.example.projectcab302.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardTest {

    private Flashcard card;
    private Teacher testUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testUser = new Teacher("asd", "asd@gmail.com", User.Roles.Teacher, "123");
        testCourse = new Course("Siuu", testUser);
        card = new Flashcard(testUser, testCourse, "What is Java?", "A programming language");
    }

    // Constructor: happy path & trimming
    @Test
    void constructor_trimsAllFields() {
        Flashcard c = new Flashcard(testUser, testCourse, "  What?  ", "  Answer  ");
        assertEquals("What?", c.getQuestion());
        assertEquals("Answer", c.getAnswer());
    }

    // Constructor: nulls throw
    @Test
    void constructor_nullUser_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(null, testCourse, "Q", "A"));
    }

    @Test
    void constructor_nullCourse_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(testUser, null, "Q", "A"));
    }

    @Test
    void constructor_nullQuestion_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(testUser, testCourse, null, "A"));
    }

    @Test
    void constructor_nullAnswer_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(testUser, testCourse, "Q", null));
    }

    // Constructor: blanks throw
    @Test
    void constructor_blankQuestion_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(testUser, testCourse, "   ", "A"));
    }

    @Test
    void constructor_blankAnswer_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Flashcard(testUser, testCourse, "Q", "   "));
    }

    // Setters: trimming
    @Test
    void setCourse_trimsAndSets() {
        Course newCourse = new Course("  CAB203  ", testUser);
        card.setCourse(newCourse);
        assertEquals(newCourse, card.getCourse());
        assertEquals("CAB203", card.getCourse().getTitle().trim());
    }

    @Test
    void setQuestion_trims() {
        card.setQuestion(" \tWhat?\n ");
        assertEquals("What?", card.getQuestion());
    }

    @Test
    void setAnswer_trims() {
        card.setAnswer("  42  ");
        assertEquals("42", card.getAnswer());
    }

    // Setters: null/blank throw
    @Test
    void setCourse_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setCourse(null));
    }

    @Test
    void setQuestion_nullOrBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setQuestion(null));
        assertThrows(IllegalArgumentException.class, () -> card.setQuestion("   "));
    }

    @Test
    void setAnswer_nullOrBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> card.setAnswer(null));
        assertThrows(IllegalArgumentException.class, () -> card.setAnswer("   "));
    }

    // ID behavior
    @Test
    void id_defaultsToZero_andCanBeSet() {
        Flashcard c = new Flashcard(testUser, testCourse, "Q", "A");
        assertEquals(0, c.getId());
        c.setId(123);
        assertEquals(123, c.getId());
    }
}
