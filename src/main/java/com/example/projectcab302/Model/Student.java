package com.example.projectcab302.Model;

/**
 * Represents a student user

 */
public class Student extends User{


    public Student(String username, String email, Roles role, String password) {
        super(username, email, role, password);
    }

    /**
     * Returns the user's Role
     *
     * @return the user Role
     */
    @Override
    public String getRole() {
        return "Student";
    }


}
