package com.example.projectcab302.Model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Course}.
 */
class CourseTest {



    private IFlashcardDAO dao;
    private Course course;

    private static Flashcard fc(String course, String q, String a) {
        return new Flashcard(course, q, a);
    }

    @BeforeEach
    void setUp() {

        course = new Course("CAB402");
    }

    // 1
    @Test
    void constructor_withTrimmedTitle_storesTrimmed() {
        Course c = new Course("  CAB203  ");
        assertEquals("CAB203", c.getTitle(), "Title should be trimmed in constructor");
    }

    // 2
    @Test
    void constructor_withNullTitle_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course(null));
    }

    // 3
    @Test
    void constructor_withBlankTitle_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Course("   "));
    }



    // 5
    @Test
    void setTitle_trimsAndStores() {
        course.setTitle("  CAB230 ");
        assertEquals("CAB230", course.getTitle());
    }

    // 6
    @Test
    void setTitle_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle(null));
    }

    // 7
    @Test
    void setTitle_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> course.setTitle("   "));
    }

    // 8
    @Test
    void getFlashcards_filtersByCurrentTitle() {

    }

    // 9
    @Test
    void getFlashcards_returnsEmptyWhenNoMatches() {
        Course c = new Course("NON_EXISTENT");
        List<Flashcard> result = c.getFlashcards();
        assertTrue(result.isEmpty(), "No flashcards should match a missing course");
    }

    // 10
    @Test
    void getFlashcards_reflectsTitleChange() {

        course.setTitle("CAB202");
        assertEquals(3, course.getFlashcards().size());
    }

    // 11
    @Test
    void testCourseDuplicates() {

        assertThrows(IllegalArgumentException.class, () -> course.setTitle("CAB202"));
    }

    // 12


    // 13

    // 14


    // 15
    @Test
    void id_roundTrip() {
        course.setId(42);
        assertEquals(42, course.getId());
    }
}
