package com.example.projectcab302.Model;

public class Student extends User{

    private double aveScore;
    private double pvpScore;




    public Student(String username, String email, Roles role, String password) {
        super(username, email, role, password);
    }

    @Override
    public String getRole() {
        return "Student";
    }


}
