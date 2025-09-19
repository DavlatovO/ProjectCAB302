package com.example.projectcab302.Model;

public class Teacher extends User{
    public Teacher(String username, String email, Roles role, String password) {
        super(username, email, role, password);

    }


    @Override
    public String getRole() {
        return "Student";
    }
}
