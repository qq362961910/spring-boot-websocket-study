package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.model.Greeting;
import com.jy.study.spring.websocket.study.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;


@MessageMapping("test/hello")
@Controller
public class HelloController {

    private SecurityHelper securityHelper;

    /**
     * 系统广播
     * */
    @MessageMapping
    @SendTo("/topic/test/hello")
    public Greeting hello(HelloMessage hello) {
        Greeting greeting = new Greeting();
        greeting.setContent("Hello, " + HtmlUtils.htmlEscape(hello.getUsername()) + "!");
        return greeting;
    }

    public HelloController(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }
}
