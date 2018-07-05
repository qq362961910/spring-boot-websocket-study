package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import com.sun.security.auth.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;

import java.security.Principal;


public class AuthenticationInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor {

    private static final String USER_KEY = "user";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private UserTicketService userTicketService;
    private UserRoleService userRoleService;
    private SecurityHelper securityHelper;
    private SessionHelper sessionHelper;
    private AppProperties appProperties;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
//        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message.getHeaders(), SimpMessageHeaderAccessor.class);
        String ticket = (String)simpMessageHeaderAccessor.getSessionAttributes().get("ticket");
        User user = userTicketService.queryUserByTicket(ticket);
        if(user == null) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            //拦截消息
            if(SimpMessageType.MESSAGE == simpMessageHeaderAccessor.getMessageType()) {
                logger.warn("session id: {}, without login user, discard [message]: {}", sessionId, message.getPayload());
//                throw new StompException("authentication failed exception");
            } else if(SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType()) {
                //拦截订阅(只允许订阅用户点对点消息)
                if(!simpMessageHeaderAccessor.getDestination().startsWith(appProperties.getUserDestinationPrefix())) {
                    logger.warn("session id: {}, without login user, discard [subscribe]: {} ", sessionId, simpMessageHeaderAccessor.getDestination());
                    return null;
                } else {
                    if(appProperties.getUserDestinationPrefix().concat(appProperties.getDestinationPrefix()).concat(appProperties.getError()).equals(simpMessageHeaderAccessor.getDestination())) {
                        sessionHelper.setSessionP2pSimpSubscriptionId(sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                        logger.info("record session: {}, simpSubscriptionId: {}", sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                    }
                }
            }
        } else {
            Principal principal = new UserPrincipal(user.getUsername());
            simpMessageHeaderAccessor.setUser(principal);
            setUserToMessageAttribute(simpMessageHeaderAccessor, user);
            user.setRoleList(userRoleService.queryUserRole(user.getUsername()));
        }
        return message;
    }

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
        User user = getUserFromMessageAttribute(simpMessageHeaderAccessor);
        securityHelper.setCurrentUser(user);
        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        securityHelper.clearCurrentUser();
    }

    private void setUserToMessageAttribute(SimpMessageHeaderAccessor simpMessageHeaderAccessor, User user) {
        simpMessageHeaderAccessor.getSessionAttributes().put(USER_KEY, user);
    }

    private User getUserFromMessageAttribute(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        return (User)simpMessageHeaderAccessor.getSessionAttributes().get(USER_KEY);
    }

    public AuthenticationInterceptor(UserTicketService userTicketService,
                                     UserRoleService userRoleService,
                                     SecurityHelper securityHelper,
                                     SessionHelper sessionHelper,
                                     AppProperties appProperties) {
        this.userTicketService = userTicketService;
        this.userRoleService = userRoleService;
        this.securityHelper = securityHelper;
        this.sessionHelper = sessionHelper;
        this.appProperties = appProperties;
    }
}
