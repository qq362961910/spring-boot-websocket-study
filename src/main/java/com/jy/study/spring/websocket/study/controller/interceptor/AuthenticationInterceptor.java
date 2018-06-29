package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;


public class AuthenticationInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private UserTicketService userTicketService;
    private SecurityHelper securityHelper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        String ticket = (String)SimpAttributesContextHolder.getAttributes().getAttribute("ticket");
        if(ticket != null) {
            User user = userTicketService.queryUserByTicket(ticket);
            if(user != null) {
                securityHelper.setCurrentUser(user);
                return message;
            }
        }
        String sessionId = SimpMessageHeaderAccessor.wrap(message).getSessionId();
        logger.warn("session id: {}, without login user", sessionId);
        return null;
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        return false;
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        return null;
    }

    public UserTicketService getUserTicketService() {
        return userTicketService;
    }

    public void setUserTicketService(UserTicketService userTicketService) {
        this.userTicketService = userTicketService;
    }

    public SecurityHelper getSecurityHelper() {
        return securityHelper;
    }

    public void setSecurityHelper(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

}
