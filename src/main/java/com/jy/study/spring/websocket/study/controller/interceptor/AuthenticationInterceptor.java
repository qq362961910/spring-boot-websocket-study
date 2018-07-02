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
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;


public class AuthenticationInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private UserTicketService userTicketService;
    private SecurityHelper securityHelper;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
//        SimpMessageHeaderAccessor accessor =
//            MessageHeaderAccessor.getAccessor(message.getHeaders(), SimpMessageHeaderAccessor.class);
        String ticket = (String)SimpAttributesContextHolder.getAttributes().getAttribute("ticket");
        User user = userTicketService.queryUserByTicket(ticket);
        if(user == null) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            //拦截消息
            if(SimpMessageType.MESSAGE == simpMessageHeaderAccessor.getMessageType()) {
                logger.warn("session id: {}, without login user, discard [message]: {}", sessionId, message.getPayload());
                return null;
            } else if(SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType()) {
                //拦截订阅
                if(!"/topic/p2p".equals(simpMessageHeaderAccessor.getDestination())) {
                    logger.warn("session id: {}, without login user, discard [subscribe]: {} ", sessionId, simpMessageHeaderAccessor.getDestination());
                    return null;
                }
            }
        }
        securityHelper.setCurrentUser(user);
        return message;
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
