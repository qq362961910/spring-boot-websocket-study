package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.RequestContext;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.security.Principal;


public class AuthenticationInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private SessionHelper sessionHelper;
    private AppProperties appProperties;
    private UserTicketService userTicketService;

    @Override
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        RequestContext.setRequestTimestamp(System.currentTimeMillis());
        Principal principal = SimpMessageHeaderAccessor.wrap(message).getUser();
        if(principal != null) {
            String ticket = principal.getName();
            if(!StringUtils.isEmpty(ticket)) {
                User user = userTicketService.queryUserByTicket(ticket);
                RequestContext.setUser(user);
            }
        }
        return filterMessage(message, RequestContext.getUser());
    }

    /**
     * 过滤消息
     * */
    private Message<?> filterMessage(Message<?> message, User user) {
        SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.wrap(message);
        if (user == null) {
            message = filterNonLoginMessage(message, simpMessageHeaderAccessor);
        }
        if((SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType())) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            if(message == null) {
                logger.warn("session: {} force discard subscribe topic: {} ", sessionId, simpMessageHeaderAccessor.getDestination());
            } else {
                logger.info("session: {} subscribe topic: {} subscriptionId: {}", sessionId, simpMessageHeaderAccessor.getDestination(), simpMessageHeaderAccessor.getSubscriptionId());
            }
        }
        return message;
    }
    private Message<?> filterNonLoginMessage(Message<?> message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        if((SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType())) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            //只允许订阅匿名topic
            if(appProperties.getAnonymousTopicSet().contains(simpMessageHeaderAccessor.getDestination())
                || ANT_PATH_MATCHER.match(appProperties.getAnonymousBroadcastTopicPattern(), simpMessageHeaderAccessor.getDestination())
                || ANT_PATH_MATCHER.match(appProperties.getAnonymousUserTopicPattern(), simpMessageHeaderAccessor.getDestination())) {
                sessionHelper.setSessionP2pErrorSimpSubscriptionId(sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                return message;
            } else {
                return null;
            }
        } else {
            return message;
        }
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {
        RequestContext.clearContext();
    }

    public AuthenticationInterceptor(SessionHelper sessionHelper, AppProperties appProperties, UserTicketService userTicketService) {
        this.sessionHelper = sessionHelper;
        this.appProperties = appProperties;
        this.userTicketService = userTicketService;
    }
}
