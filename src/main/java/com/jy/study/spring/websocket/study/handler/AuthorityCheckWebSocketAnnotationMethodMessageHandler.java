package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.*;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;


public class AuthorityCheckWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private AppProperties appProperties;
    private SimpMessageSendingOperations brokerTemplate;
    private SecurityHelper securityHelper;

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        AuthorityCheck authorityCheck = handlerMethod.getMethod().getAnnotation(AuthorityCheck.class);
        if(authorityCheck == null) {
            authorityCheck = handlerMethod.getBeanType().getAnnotation(AuthorityCheck.class);
        }
        String errorMessage = null;
        if(authorityCheck != null) {
            //登录检查
            User user = securityHelper.getCurrentUser();
            if(user == null) {
                errorMessage = "no user login";
            }
            if(errorMessage == null) {
                //权限检查
                String[] roles = authorityCheck.roles();
                if(roles != null && roles.length > 0) {
                    errorMessage = "no authority";
                    for(String roleStr: roles) {
                        for(String role: user.getRoleList()) {
                            if(role.equals(roleStr)) {
                                errorMessage = null;
                            }
                        }
                    }
                }
            }
            if(errorMessage != null) {
                SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
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
                                                                 SecurityHelper securityHelper,
                                                                 AppProperties appProperties) {
        super(clientInChannel, clientOutChannel, brokerTemplate);
        this.brokerTemplate = brokerTemplate;
        this.securityHelper = securityHelper;
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
