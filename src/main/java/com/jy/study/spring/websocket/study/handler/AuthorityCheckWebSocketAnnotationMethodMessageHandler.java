package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.config.GenericPrincipal;
import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.*;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;

import java.security.Principal;


public class AuthorityCheckWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private AppProperties appProperties;
    private SimpMessageSendingOperations brokerTemplate;

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        AuthorityCheck authorityCheck = handlerMethod.getMethod().getAnnotation(AuthorityCheck.class);
        if(authorityCheck == null) {
            authorityCheck = handlerMethod.getBeanType().getAnnotation(AuthorityCheck.class);
        }
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
        String errorMessage = null;
        if(authorityCheck != null) {
            //登录检查
            Principal principal = simpMessageHeaderAccessor.getUser();
            if(principal == null) {
                errorMessage = "no user login";
            }
            if(errorMessage == null) {
                if(principal instanceof GenericPrincipal) {
                    GenericPrincipal genericPrincipal = (GenericPrincipal)principal;
                    //权限检查
                    String[] roles = authorityCheck.roles();
                    if(roles != null && roles.length > 0) {
                        errorMessage = "no authority";
                        out: for(String roleStr: roles) {
                            for(String role: genericPrincipal.getRoles()) {
                                if(role.equals(roleStr)) {
                                    errorMessage = null;
                                    break out;
                                }
                            }
                        }
                    }
                } else {
                    errorMessage = "no authority";
                }
            }
            if(errorMessage != null) {
                MethodParameter returnType = handlerMethod.getReturnType();
                this.brokerTemplate.convertAndSendToUser(simpMessageHeaderAccessor.getSessionId(), appProperties.getUserErrorTopic(), errorMessage, createHeaders(simpMessageHeaderAccessor.getSessionId(), returnType));
                return;
            }
        }
        super.handleMatch(mapping, handlerMethod, lookupDestination, message);
    }

    public AuthorityCheckWebSocketAnnotationMethodMessageHandler(SubscribableChannel clientInChannel,
                                                                 MessageChannel clientOutChannel,
                                                                 SimpMessageSendingOperations brokerTemplate,
                                                                 AppProperties appProperties) {
        super(clientInChannel, clientOutChannel, brokerTemplate);
        this.brokerTemplate = brokerTemplate;
        this.appProperties = appProperties;
    }

    private MessageHeaders createHeaders(@Nullable String sessionId, MethodParameter returnType) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        if (getHeaderInitializer() != null) {
            getHeaderInitializer().initHeaders(headerAccessor);
        }
        if (sessionId != null) {
            headerAccessor.setSessionId(sessionId);
        }
        headerAccessor.setHeader(SimpMessagingTemplate.CONVERSION_HINT_HEADER, returnType);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
