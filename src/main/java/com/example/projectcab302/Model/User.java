package com.example.projectcab302.Model;

import com.example.projectcab302.Utils.Hashing;

public abstract class User {
    private int id;
    private String username;
    private String email;
    private Roles role;
    private String password;


    public enum Roles{
        Student,
        Teacher
    }

    public User(String username, String email, Roles role, String password) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {return email;}
    public Roles getRoles() {return role;}
    public String getHashedPassword() {
        return Hashing.hashPassword(password);
    }
    public String getUsername() {return username;}
    public abstract String getRole();


}


