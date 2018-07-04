package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@MessageMapping("auth")
@Controller
public class AuthController {

    private SecurityHelper securityHelper;

    @SendToUser("/topic/auth/need_login")
    @AuthorityCheck(roles = {"admin"})
    @MessageMapping("need_login")
    public String needLogin() {
        return String.format("login user: %s", securityHelper.getCurrentUser().getUsername());
    }

    @SendToUser("/topic/auth/no_need_login")
    @MessageMapping("no_need_login")
    public String noNeedLogin() {
        return "no need login";
    }

    public AuthController(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }
}
