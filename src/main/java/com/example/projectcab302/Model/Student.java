package com.example.projectcab302.Model;

public class Student extends User{

    private double aveScore;

    public Student(String username, String email, Roles role, String password, double aveScore) {
        super(username, email, role, password);
        this.aveScore = aveScore;
    }

    public Student(String username, String email, Roles role, String password) {
        super(username, email, role, password);
    }
    @Override
    public String getRole() {
        return "Student";
    }

    public double getAveScore() {return aveScore;}
    public void setAveScore() {this.aveScore = (this.aveScore + aveScore)/2 ;}
}
