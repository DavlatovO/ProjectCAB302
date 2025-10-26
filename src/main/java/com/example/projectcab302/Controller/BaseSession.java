package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.Student;
import com.example.projectcab302.Model.User;

/**
 * Base class for controllers that require a user session reference.
 * <p>
 * Provides common functionality for assigning and handling a logged-in user.
 */
public abstract class BaseSession {

    /** The current user associated with this session. */
    protected User user;

    /**
     * Sets the current user and triggers {@code afterUserisSet()}.
     *
     * @param user the user to assign
     */
    public void setUser(User user) {
        System.out.println(user.getUsername());
        this.user = user;
        afterUserisSet();
    }

    /**
     * Called automatically after a user is set.
     * Subclasses may override to perform setup tasks.
     */
    public void afterUserisSet() {}
}
