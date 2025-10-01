package com.example.projectcab302.Model;

public class Teacher extends User{
    public Teacher(String username, String email, Roles role, String password) {
        super(username, email, role, password);

    }

    //Should probably make this enum later
    @Override
    public String getRole() {
        return "Teacher";
    }
}
