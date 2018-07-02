package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.exception.StompException;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

public class AppStompErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        if(ex instanceof StompException) {
            StompException stompException = (StompException)ex;
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
            headerAccessor.setLeaveMutable(true);
            return MessageBuilder.createMessage(stompException.getBody().getBytes(), headerAccessor.getMessageHeaders());
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }
}
