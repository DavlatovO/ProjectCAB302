package com.example.projectcab302.Controller;

/**
 * Base class for controllers that require a user session (from BaseSession)
 * and a reference to an FXML view path.
 * <p>
 * Used in the CoursesController to determine navigation after a course is selected.
 */
public abstract class BaseFXMLandSession extends BaseSession {

    /** Path to the FXML file to navigate to. */
    protected String fxml;

    /**
     * Sets the FXML file path.
     *
     * @param fxml the FXML path to assign
     */
    public void setFXML(String fxml) {
        this.fxml = fxml;
    }
}
