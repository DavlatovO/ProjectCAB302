package com.example.projectcab302.Controller;

import com.example.projectcab302.Model.Course;
import com.example.projectcab302.Model.User;

public abstract class BaseSession {
    protected static User user;

    public static void setUser(User user) {
        System.out.println(user.getUsername());
        BaseSession.user = user;
    }


    public static User getUser() {
        return user;
    }

    public static void clear() {
        user = null;
    }

}
