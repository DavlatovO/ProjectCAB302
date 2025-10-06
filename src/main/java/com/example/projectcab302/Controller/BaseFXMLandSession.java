package com.example.projectcab302.Controller;

// Used to specify clicking on a course will do in the courses page
public abstract class BaseFXMLandSession extends BaseSession {
    protected String fxml;
    public void setFXML(String fxml) {
        this.fxml = fxml;
    }
}
