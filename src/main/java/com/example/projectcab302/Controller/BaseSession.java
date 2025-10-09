package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.User;

public abstract class BaseSession {
    protected  User user;

    public void setUser(User user) {
        this.user = user;
        System.out.println(user.getUsername());
    }


    public User getUser() {
        return user;
    }

    public  void clear() {
        user = null;
    }

}
