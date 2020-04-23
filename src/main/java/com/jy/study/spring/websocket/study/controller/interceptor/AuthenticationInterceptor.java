package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
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

import java.util.Map;


public class AuthenticationInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final String USER_KEY = "user";

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
        return doWithMessage(message, simpMessageHeaderAccessor, simpMessageHeaderAccessor.getSessionAttributes());
    }

    /**
     * 过滤匿名消息, 设置当前登陆用户到context
     */
    private Message<?> doWithMessage(Message<?> message, SimpMessageHeaderAccessor simpMessageHeaderAccessor, Map<String, Object> sessionAttributes) {
        if(sessionAttributes == null) {
            return filterNonLoginMessage(message, simpMessageHeaderAccessor);
        } else {
            User user = (User)sessionAttributes.get("user");
            if (user == null) {
                return filterNonLoginMessage(message, simpMessageHeaderAccessor);
            } else {
                return message;
            }
        }
    }
    private Message<?> filterNonLoginMessage(Message<?> message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        if((SimpMessageType.SUBSCRIBE == simpMessageHeaderAccessor.getMessageType())) {
            String sessionId = simpMessageHeaderAccessor.getSessionId();
            //只允许订阅匿名topic
            if(appProperties.getAnonymousTopicSet().contains(simpMessageHeaderAccessor.getDestination())
                || ANT_PATH_MATCHER.match(appProperties.getAnonymousBroadcastTopicPattern(), simpMessageHeaderAccessor.getDestination())
                || ANT_PATH_MATCHER.match(appProperties.getAnonymousUserTopicPattern(), simpMessageHeaderAccessor.getDestination())) {
                sessionHelper.setSessionP2pErrorSimpSubscriptionId(sessionId, simpMessageHeaderAccessor.getSubscriptionId());
                logger.info("session: {} subscribe anonymous topic: {} subscriptionId: {}", sessionId, simpMessageHeaderAccessor.getDestination(), simpMessageHeaderAccessor.getSubscriptionId());
                return message;
            } else {
                logger.warn("session id: {}, without login user, discard [subscribe]: {} ", sessionId, simpMessageHeaderAccessor.getDestination());
                return null;
            }
        } else {
            return message;
        }
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

    private User getUserFromMessageAttribute(SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Map<String, Object> sessionAttributes =  simpMessageHeaderAccessor.getSessionAttributes();
        if(sessionAttributes == null) {
            return null;
        }
        return (User)sessionAttributes.get(USER_KEY);
    }

    public AuthenticationInterceptor(SecurityHelper securityHelper,
                                     SessionHelper sessionHelper,
                                     AppProperties appProperties) {
        this.securityHelper = securityHelper;
        this.sessionHelper = sessionHelper;
        this.appProperties = appProperties;
    }
}
