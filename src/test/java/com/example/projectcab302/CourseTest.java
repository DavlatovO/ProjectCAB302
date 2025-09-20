package com.example.projectcab302.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        // Each test will query the SQLite DB through SqliteCoursesDAO
        course = new Course("CAB402");
    }

    @Test
    void constructor_trimsTitle() {
        Course c = new Course("   CAB203   ");
        assertEquals("CAB203", c.getTitle());
    }

    @Test
    void constructor_withNull_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course(null));
    }

    @Test
    void constructor_withBlank_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course("   "));
    }

    @Test
    void setTitle_trimsAndStores() {
        course.setTitle("   CAB230   ");
        assertEquals("CAB230", course.getTitle());
    }

    @Test
    void setTitle_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle(null));
    }

    @Test
    void setTitle_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle("   "));
    }

    @Test
    void setTitle_duplicate_throws() {
        // This will only throw if your DB already contains "CAB202"
        assertThrows(IllegalArgumentException.class, () -> course.setTitle("CAB202"));
    }

    @Test
    void id_roundTrip() {
        course.setId(123);
        assertEquals(123, course.getId());
    }

    @Test
    void getFlashcards_returnsOnlyMatching() {
        course.setTitle("CAB202");
        List<Flashcard> cards = course.getFlashcards();

        // depends on your sample data in SqliteFlashcardDAO
        assertTrue(cards.stream().allMatch(c -> c.getCourse().equals("CAB202")));
    }

    @Test
    void getTransferredTitle_roundTrip() {
        Course.setTransferredTitle("test");
        assertEquals("test", Course.getTransferredTitle());
    }
}
