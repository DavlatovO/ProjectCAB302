package com.example.projectcab302.Model;

import java.util.List;

/**
 * Interface for the Contact Data Access Object that handles
 * the CRUD operations for the Contact class with the database.
 */

public interface ICoursesDAO {


 /**
     * Adds a new contact to the database.
     * @param course The contact to add.
     */
    public void addCourse(Course course);
    /**
     * Updates an existing contact in the database.
     * @param course The contact to update.
     */
    public void updateCourse(Course course);
    /**
     * Deletes a contact from the database.
     * @param course The contact to delete.
     */
    public void deleteCourse(Course course);
    /**
     * Retrieves a contact from the database.
     * @param id The id of the contact to retrieve.
     * @return The contact with the given id, or null if not found.
     */
    public Course getCourse(int id);
    /**
     * Retrieves all contacts from the database.
     * @return A list of all contacts in the database.
     */
    public List<Course> getAllCourses();

    public void clearData();

    void insertSampleData();
}