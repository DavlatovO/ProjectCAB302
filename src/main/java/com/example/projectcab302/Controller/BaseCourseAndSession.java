package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
/**
 * Base class for controllers requiring both a user (from BaseSession)
 * and a course reference.
 */
public abstract class BaseCourseAndSession extends BaseSession {

    /** The current course associated with this controller. */
    protected Course course;

    /**
     * Called automatically after a course is set.
     * Implemented by subclasses to handle setup tasks.
     */
    protected abstract void afterCourseisSet();

    /**
     * Sets the current course and triggers {@code afterCourseisSet()}.
     *
     * @param course the course to assign
     */
    public void setCourse(Course course) {
        this.course = course;
        afterCourseisSet();
    }
}


