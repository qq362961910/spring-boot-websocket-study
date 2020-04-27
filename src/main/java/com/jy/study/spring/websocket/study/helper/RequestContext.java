package com.jy.study.spring.websocket.study.helper;

import com.jy.study.spring.websocket.study.config.GenericPrincipal;

public class RequestContext {

    private static final ThreadLocal<GenericPrincipal> userHolder = new ThreadLocal<>();
    private static final ThreadLocal<Long> requestTimestamp = new ThreadLocal<>();

    public static void setCurrentUser(GenericPrincipal user) {
        if(user != null) {
            userHolder.set(user);
        }
    }

    public static GenericPrincipal getCurrentUser() {
        return userHolder.get();
    }

    public static void setRequestTimestamp(Long timestamp) {
        requestTimestamp.set(timestamp);
    }

    public static Long getRequestTimestamp() {
        return requestTimestamp.get();
    }

    public static void clearCurrentUser() {
        userHolder.remove();
        requestTimestamp.remove();
    }

}
