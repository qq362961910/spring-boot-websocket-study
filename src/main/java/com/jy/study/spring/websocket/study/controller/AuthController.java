package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.helper.RequestContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@MessageMapping("auth")
@Controller
public class AuthController {

    @SendToUser("/topic/p2p")
    @AuthorityCheck(roles = {"admin"})
    @MessageMapping("need_login")
    public String needLogin() {
        return String.format("login user: %s", RequestContext.getUser().getUsername());
    }

    @SendToUser("/topic/p2p")
    @MessageMapping("no_need_login")
    public String noNeedLogin() {
        return "no need login";
    }

}
