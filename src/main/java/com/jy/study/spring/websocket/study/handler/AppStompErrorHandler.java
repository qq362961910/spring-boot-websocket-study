package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.exception.StompException;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

/**
 * 异常处理，响应到用户p2p error topic
 * */
public class AppStompErrorHandler extends StompSubProtocolErrorHandler {

    private SessionHelper sessionHelper;
    private AppProperties appProperties;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        if(ex instanceof StompException) {
            SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(clientMessage);
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            SessionHelper.SessionConfig sessionConfig = sessionHelper.getUserSessionConfig(sessionId, false);
            if(sessionConfig != null) {
                StompException stompException = (StompException)ex;
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
                String destination = headerAccessor.getDestination().replaceAll(appProperties.getApplicationDestinationPrefix(), appProperties.getUserDestinationPrefix());
                headerAccessor.setDestination(destination);
                headerAccessor.setSubscriptionId(sessionConfig.getP2pErrorSimpSubscriptionId());
                headerAccessor.setLeaveMutable(true);
                return MessageBuilder.createMessage(stompException.getBody().getBytes(), headerAccessor.getMessageHeaders());
            }
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    public AppStompErrorHandler(SessionHelper sessionHelper, AppProperties appProperties) {
        this.sessionHelper = sessionHelper;
        this.appProperties = appProperties;
    }
}
