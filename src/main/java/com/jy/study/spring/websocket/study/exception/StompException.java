package com.jy.study.spring.websocket.study.exception;

import org.springframework.messaging.MessagingException;

public class StompException extends MessagingException {

    private String body;

    public StompException(String body) {
        super(body);
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
