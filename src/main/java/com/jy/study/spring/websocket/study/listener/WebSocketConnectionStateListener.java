package com.jy.study.spring.websocket.study.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class WebSocketConnectionStateListener implements ApplicationListener<AbstractSubProtocolEvent> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConnectionStateListener.class);

    @Override
    public void onApplicationEvent(AbstractSubProtocolEvent event) {
        if(event instanceof SessionConnectedEvent) {
            SessionConnectedEvent connectedEvent = (SessionConnectedEvent)event;
            Object source = connectedEvent.getSource();
            Message<byte[]> message = connectedEvent.getMessage();
            logger.info("a new web-socket connection established,");
        } else if(event instanceof SessionDisconnectEvent) {
            SessionDisconnectEvent disconnectEvent = (SessionDisconnectEvent)event;
            Object source = disconnectEvent.getSource();
            Message<byte[]> message = disconnectEvent.getMessage();
            String sesionId = disconnectEvent.getSessionId();
            CloseStatus status = disconnectEvent.getCloseStatus();
            logger.info("a web-socket disconnected from server");
        }
    }
}
