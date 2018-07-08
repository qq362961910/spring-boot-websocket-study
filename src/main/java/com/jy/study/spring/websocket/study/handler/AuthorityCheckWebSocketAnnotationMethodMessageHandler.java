package com.jy.study.spring.websocket.study.handler;

import com.jy.study.spring.websocket.study.anno.AuthorityCheck;
import com.jy.study.spring.websocket.study.entity.Role;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.SimpMessageMappingInfo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.WebSocketAnnotationMethodMessageHandler;


public class AuthorityCheckWebSocketAnnotationMethodMessageHandler extends WebSocketAnnotationMethodMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthorityCheckWebSocketAnnotationMethodMessageHandler.class);

    private SecurityHelper securityHelper;

    @Override
    protected void handleMatch(SimpMessageMappingInfo mapping, HandlerMethod handlerMethod, String lookupDestination, Message<?> message) {
        AuthorityCheck authorityCheck = handlerMethod.getMethod().getAnnotation(AuthorityCheck.class);
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
                        for(Role role: user.getRoleList()) {
                            if(role.getName().equals(roleStr)) {
                                errorMessage = null;
                            }
                        }
                    }
                }
            }
            if(errorMessage != null) {
                //禁止未登录用户广播消息
                MethodParameter returnType = handlerMethod.getReturnType();
                for(HandlerMethodReturnValueHandler handler: this.getReturnValueHandlers()) {
                    if(handler.supportsReturnType(returnType)) {
                        try { handler.handleReturnValue(errorMessage, returnType, message); } catch (Exception e) { e.printStackTrace(); }
                        break;
                    }
                }
                return;
            }
        }
        super.handleMatch(mapping, handlerMethod, lookupDestination, message);
    }

    public AuthorityCheckWebSocketAnnotationMethodMessageHandler(SubscribableChannel clientInChannel, MessageChannel clientOutChannel, SimpMessageSendingOperations brokerTemplate, SecurityHelper securityHelper) {
        super(clientInChannel, clientOutChannel, brokerTemplate);
        this.securityHelper = securityHelper;
    }
}
