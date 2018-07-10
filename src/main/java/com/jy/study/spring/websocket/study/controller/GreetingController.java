package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.model.Greeting;
import com.jy.study.spring.websocket.study.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;


@Controller
public class GreetingController {

    private SecurityHelper securityHelper;

    /**
     * 系统广播
     * */
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")//broadcast to all subscribers "/topic/greetings"
    public Greeting greeting(HelloMessage hello) {
        Greeting greeting = new Greeting();
        greeting.setContent("Hello, " + HtmlUtils.htmlEscape(hello.getUsername()) + "!");
        return greeting;
    }

    /**
     * 自言自语
     * */
    @SendToUser("/topic/echo")
    @MessageMapping("/echo")
    public String echo(String message) {
        return message;
    }

    public GreetingController(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }
}
