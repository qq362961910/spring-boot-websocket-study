package com.jy.study.spring.websocket.study.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * echo string
 *
 * @author yj
 * @since 2020-04-21 12:20
 **/
@MessageMapping("test/echo")
@Controller
public class EchoController {

    /**
     * 自言自语
     * */
    @SendToUser(value = "/topic/test/echo", broadcast = false)
    @MessageMapping
    public String echo(String message) {
        return message;
    }
}
