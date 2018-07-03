package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@MessageMapping("chat")
@Controller
public class ChantController {

    private SecurityHelper securityHelper;

    @SendTo("/broadcast")
    public String broadcast(String content) {
        User user = securityHelper.getCurrentUser();
        return String.format("%s say: %s", user.getUsername(), content);
    }

    public ChantController(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }
}
