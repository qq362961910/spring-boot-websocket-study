package com.jy.study.spring.websocket.study.helper;

import com.jy.study.spring.websocket.study.entity.User;

public class RequestContext {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> requestTimestamp = new ThreadLocal<>();

    public static void setUser(User user) {
        if(user != null) {
            userHolder.set(user);
        }
    }

    public static User getUser() {
        return userHolder.get();
    }

    public static void setRequestTimestamp(Long timestamp) {
        requestTimestamp.set(timestamp);
    }

    public static Long getRequestTimestamp() {
        return requestTimestamp.get();
    }

    public static void clearContext() {
        userHolder.remove();
        requestTimestamp.remove();
    }

}
