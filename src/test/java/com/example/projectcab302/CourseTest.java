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
        // Seed data with mixed courses

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

    // 4
    @Test
    void constructor_withNullDao_throwsNullPointer() {
        assertThrows(NullPointerException.class, () -> new Course("CAB302"));
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
        List<Flashcard> result = course.getFlashcards();
        assertEquals(2, result.size(), "Should return only CAB302 cards");
        assertTrue(result.stream().allMatch(f -> "CAB302".equals(f.getCourse())));
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
        // Initially CAB302 has 2
        assertEquals(2, course.getFlashcards().size());
        // Change to CAB201 which has 1
        course.setTitle("CAB201");
        assertEquals(1, course.getFlashcards().size());
    }

    // 11
    @Test
    void getFlashcards_resultMutationDoesNotAffectSubsequentCalls() {
        List<Flashcard> first = course.getFlashcards();
        assertEquals(2, first.size());
        // Mutate caller's copy
        first.clear();
        // Fresh call should rebuild and still have items
        List<Flashcard> second = course.getFlashcards();
        assertEquals(2, second.size(), "Method should return a fresh filtered list each time");
    }

    // 12
    @Test
    void transferredTitle_set_trims() {
        Course.setTransferredTitle("  Alpha  ");
        assertEquals("Alpha", Course.getTransferredTitle());
    }

    // 13
    @Test
    void transferredTitle_set_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> Course.setTransferredTitle(null));
    }

    // 14
    @Test
    void transferredTitle_set_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> Course.setTransferredTitle("   "));
    }

    // 15
    @Test
    void id_roundTrip() {
        course.setId(42);
        assertEquals(42, course.getId());
    }
}
