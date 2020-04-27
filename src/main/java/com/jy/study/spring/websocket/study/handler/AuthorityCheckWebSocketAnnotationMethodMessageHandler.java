package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.RequestContext;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.*;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;


public class AuthorityCheckWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private static final String NO_USER_LOGIN_MSG = "no user login";
    private static final String NO_AUTHORITY = "no authority";

    private AppProperties appProperties;
    private SimpMessageSendingOperations brokerTemplate;

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        AuthorityCheck authorityCheck = handlerMethod.getMethod().getAnnotation(AuthorityCheck.class);
        if(authorityCheck == null) {
            authorityCheck = handlerMethod.getBeanType().getAnnotation(AuthorityCheck.class);
        }
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
        if(authorityCheck != null) {
            //登录检查
            User user = RequestContext.getUser();
            MethodParameter returnType = handlerMethod.getReturnType();
            if(user == null) {
                if(!StringUtils.isEmpty(simpMessageHeaderAccessor.getSessionId())) {
                    this.brokerTemplate.convertAndSendToUser(simpMessageHeaderAccessor.getSessionId(),
                        appProperties.getUserErrorTopic().replace(appProperties.getUserDestinationPrefix(), ""),
                        NO_USER_LOGIN_MSG,
                        createHeaders(simpMessageHeaderAccessor.getSessionId(), returnType));
                }
                //todo 断开连接
                return;
            } else {
                //权限检查
                String[] roles = authorityCheck.roles();
                if(roles != null && roles.length > 0) {
                    boolean authorized = false;
                    out: for(String roleStr: roles) {
                        for(String role: user.getRoleList()) {
                            if(role.equals(roleStr)) {
                                authorized = true;
                                break out;
                            }
                        }
                    }
                    if(!authorized) {
                        if(!StringUtils.isEmpty(simpMessageHeaderAccessor.getSessionId())) {
                            this.brokerTemplate.convertAndSendToUser(simpMessageHeaderAccessor.getSessionId(),
                                appProperties.getUserErrorTopic().replace(appProperties.getUserDestinationPrefix(), ""),
                                NO_AUTHORITY,
                                createHeaders(simpMessageHeaderAccessor.getSessionId(),returnType));
                        }
                        return;
                    }
                }
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
