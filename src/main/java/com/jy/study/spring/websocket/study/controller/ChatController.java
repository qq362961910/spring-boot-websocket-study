package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.model.BroadcastParam;
import com.jy.study.spring.websocket.study.model.BroadcastResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@MessageMapping("test/chat")
@Controller
public class ChatController {

    @MessageMapping("broadcast")
    @SendTo("/topic/test/chat/broadcast")
    public BroadcastResult broadcast(BroadcastParam broadcastParam) {
        BroadcastResult result = new BroadcastResult();
        result.setMsg(broadcastParam.getContent());
        return result;
    }

}
