package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ExecutorChannelInterceptor;

import java.util.HashMap;
import java.util.Map;


public class AuthenticationInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor {

    private static final String USER_KEY = "user";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private UserTicketService userTicketService;
    private UserRoleService userRoleService;
    private SecurityHelper securityHelper;
    private SessionHelper sessionHelper;
    private AppProperties appProperties;

    /**
     * 拦截用户订阅消息
     * 如果用户未登录只允许订阅·p2p·topic
     * 如果用户已登录就把消息放进消息头中
     * */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
//        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message.getHeaders(), SimpMessageHeaderAccessor.class);
        String ticket = (String)simpMessageHeaderAccessor.getSessionAttributes().get("ticket");
        User user = userTicketService.queryUserByTicket(ticket);
        if(user == null) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            //订阅消息
            if(SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType()) {
                if(canSubscribeWithoutLogin(simpMessageHeaderAccessor.getDestination())) {
                    sessionHelper.setSessionP2pErrorSimpSubscriptionId(sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                    logger.info("record session: {}, error topic subscriptionId: {}", sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                } else {
                    logger.warn("session id: {}, without login user, discard [subscribe]: {} ", sessionId, simpMessageHeaderAccessor.getDestination());
                    return null;
                }
            }
        } else {
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
        Map<String, Object> sessionAttributes =  simpMessageHeaderAccessor.getSessionAttributes();
        if(sessionAttributes == null) {
            sessionAttributes = new HashMap<>();
            simpMessageHeaderAccessor.setSessionAttributes(sessionAttributes);
        }
        sessionAttributes.put(USER_KEY, user);
    }

    private User getUserFromMessageAttribute(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Map<String, Object> sessionAttributes =  simpMessageHeaderAccessor.getSessionAttributes();
        if(sessionAttributes == null) {
            return null;
        }
        return (User)sessionAttributes.get(USER_KEY);
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

    private boolean canSubscribeWithoutLogin(String topic) {
        return appProperties.getUserError().equals(topic) || appProperties.getApplicationBroadcastTopic().equals(topic);
    }
}
