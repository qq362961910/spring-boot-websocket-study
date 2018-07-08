package com.jy.study.spring.websocket.study.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

@RequestMapping("websocket")
@RestController
public class WebSocketMessageBrokerStatsController {

    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    @RequestMapping("state")
    public Object state() {
        return webSocketMessageBrokerStats.toString();
    }

    public WebSocketMessageBrokerStatsController(WebSocketMessageBrokerStats webSocketMessageBrokerStats) {
        this.webSocketMessageBrokerStats = webSocketMessageBrokerStats;
    }
}
