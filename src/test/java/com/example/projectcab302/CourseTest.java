package com.example.projectcab302.Model;

import com.example.projectcab302.Persistence.ICoursesDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;


    @BeforeEach
    void setUp() {
        // Fixture : constructs Course (current design hits SQLite via DAOs → integration-style tests)
        course = new Course("CAB402");
    }

    // ───────────── Constructor & title validation ─────────────
    // Constructor & title validation — trims leading/trailing whitespace
    @Test
    void constructor_trimsTitle() {
        Course c = new Course("   CAB203   ");
        assertEquals("CAB203", c.getTitle());
    }

    // Constructor & title validation — throws on null title
    @Test
    void constructor_withNull_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course(null));
    }

    // Constructor & title validation — throw  s on blank "   " title
    @Test
    void constructor_withBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course("   "));
    }

    // ───────────── Title Setting ─────────────
    // Title setting — setTitle stores the trimmed value
    @Test
    void setTitle_trimsAndStores() {
        course.setTitle("   CAB230   ");
        assertEquals("CAB230", course.getTitle());
    }

    // Title setting — throws on null
    @Test
    void setTitle_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle(null));
    }

    // Title setting — throws on blank
    @Test
    void setTitle_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle("   "));
    }

    // Title setting — duplicate title rule (expects throw; depends on DB/DAO having "CAB202")


    // ───────────── Id round-trip ─────────────
    // Id round-trip — setId then getId returns the same value
    @Test
    void id_roundTrip() {
        course.setId(123);
        assertEquals(123, course.getId());
    }

    // ───────────── Id round-trip ─────────────
    // Flashcards filtering — returns only cards where card.getCourse().equals(currentTitle)
    @Test
    void getFlashcards_returnsOnlyMatching() {

        List<Flashcard> cards = course.getFlashcards();

        // depends on your sample data in SqliteFlashcardDAO
        assertTrue(cards.stream().allMatch(c -> c.getCourse().equals("CAB202")));
    }
}
