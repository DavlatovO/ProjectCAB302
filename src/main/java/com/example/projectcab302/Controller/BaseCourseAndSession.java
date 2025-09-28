package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;

public abstract class BaseCourseAndSession extends BaseSession{
    protected Course course;

    public abstract void afterCourseisSet();

    public void setCourse(Course Course) {
        this.course = Course;
        afterCourseisSet();
    }
}
