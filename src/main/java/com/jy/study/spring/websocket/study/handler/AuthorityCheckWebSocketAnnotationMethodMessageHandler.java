package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.simp.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;

import java.security.Principal;
import java.util.List;


public class AuthorityCheckWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private static final String NO_USER_LOGIN_MSG = "no user login";
    private static final String NO_AUTHORITY = "no authority";

    private AppProperties appProperties;
    private SimpMessageSendingOperations brokerTemplate;
    private UserRoleService userRoleService;

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        AuthorityCheck authorityCheck = handlerMethod.getMethod().getAnnotation(AuthorityCheck.class);
        if(authorityCheck == null) {
            authorityCheck = handlerMethod.getBeanType().getAnnotation(AuthorityCheck.class);
        }
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
        if(authorityCheck != null) {
            //登录检查
            Principal user = simpMessageHeaderAccessor.getUser();
            MethodParameter returnType = handlerMethod.getReturnType();
            if(user == null) {
                if(!StringUtils.isEmpty(simpMessageHeaderAccessor.getSessionId())) {
                    this.brokerTemplate.convertAndSendToUser(simpMessageHeaderAccessor.getSessionId(),
                        appProperties.getUserTopic().replace(appProperties.getUserDestinationPrefix(), ""),
                        NO_USER_LOGIN_MSG,
                        createHeaders(simpMessageHeaderAccessor.getSessionId(), returnType));
                }
                return;
            } else {
                //权限检查
                String[] roles = authorityCheck.roles();
                if(roles != null && roles.length > 0) {
                    boolean authorized = false;
                    List<String> roleList = userRoleService.queryUserRoleName(user.getName());
                    if(!CollectionUtils.isEmpty(roleList)) {
                        out: for(String roleStr: roles) {
                            for(String role: roleList) {
                                if(role.equals(roleStr)) {
                                    authorized = true;
                                    break out;
                                }
                            }
                        }
                    }
                    if(!authorized) {
                        if(!StringUtils.isEmpty(simpMessageHeaderAccessor.getSessionId())) {
                            this.brokerTemplate.convertAndSendToUser(simpMessageHeaderAccessor.getSessionId(),
                                appProperties.getUserTopic().replace(appProperties.getUserDestinationPrefix(), ""),
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

    public AuthorityCheckWebSocketAnnotationMethodMessageHandler(SubscribableChannel clientInChannel, MessageChannel clientOutChannel, SimpMessageSendingOperations brokerTemplate, AppProperties appProperties, UserRoleService userRoleService) {
        super(clientInChannel, clientOutChannel, brokerTemplate);
        this.appProperties = appProperties;
        this.brokerTemplate = brokerTemplate;
        this.userRoleService = userRoleService;
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
