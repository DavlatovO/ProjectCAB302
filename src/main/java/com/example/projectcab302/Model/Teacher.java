package com.example.projectcab302.Model;

/**
 * Represents a Teacher user

 */
public class Teacher extends User{
    public Teacher(String username, String email, Roles role, String password) {
        super(username, email, role, password);

    }

    /**
     * Returns the user's Role
     *
     * @return the user Role
     */
    @Override
    public String getRole() {
        return "Teacher";
    }
}
