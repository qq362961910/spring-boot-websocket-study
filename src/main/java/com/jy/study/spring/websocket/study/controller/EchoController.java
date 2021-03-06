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
@Controller
public class EchoController {

    /**
     * 自言自语
     * */
    @SendToUser("/topic/p2p")
    @MessageMapping("echo")
    public String echo(String message) {
        return message;
    }
}
