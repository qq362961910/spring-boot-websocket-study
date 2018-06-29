package com.jy.study.spring.websocket.study.helper;

import com.jy.study.spring.websocket.study.entity.User;

public class SecurityHelper {

    private final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public void setCurrentUser(User user) {
        if(user != null) {
            userHolder.set(user);
        }
    }

    public User getCurrentUser() {
        return userHolder.get();
    }

    public void clearCurrentUser() {
        userHolder.remove();
    }

}
