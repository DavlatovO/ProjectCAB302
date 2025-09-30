package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Course;

import java.util.List;

/**
 * Interface for the course Data Access Object that handles
 * the CRUD operations for the course class with the database.
 */

public interface ICoursesDAO {

 /**
     * Adds a new course to the database.
     * @param course The course to add.
     */
    public void addCourse(Course course);
    /**
     * Updates an existing course in the database.
     * @param course The course to update.
     */
    public void updateCourse(Course course);
    /**
     * Deletes a course from the database.
     * @param course The course to delete.
     */
    public void deleteCourse(Course course);
    /**
     * Retrieves a course from the database.
     * @param  id The id of the course to retrieve.
     * @return The course with the given id, or null if not found.
     */
    public Course getCourse(int id);
    /**
     * Retrieves all course from the database.
     * @return A list of all course in the database.
     */
    public List<Course> getAllCourses();

    /**
     * Clears all data from the database.
     */
    public void clearData();

    /**
     * Checks if a course exists in the database.
     * @param  title The title of the course to check.
     * @return A boolean value whether course exists in the database.
     */
   public Boolean checkTitleExists(String title);

    /**
     * Inserts sample data in the data base
     */
    void insertSampleData();
}