package com.jy.study.spring.websocket.study.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;

public class AppWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppWebSocketAnnotationMethodMessageHandler.class);

    public AppWebSocketAnnotationMethodMessageHandler(SubscribableChannel clientInChannel, MessageChannel clientOutChannel, SimpMessageSendingOperations brokerTemplate) {
        super(clientInChannel, clientOutChannel, brokerTemplate);
    }

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        super.handleMatch(mapping, handlerMethod, lookupDestination, message);
    }
}
