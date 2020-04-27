package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
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

import java.security.Principal;


public class AuthenticationInterceptor implements ChannelInterceptor, ExecutorChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

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
        return doWithMessage(message, simpMessageHeaderAccessor);
    }

    /**
     * 过滤匿名消息, 设置当前登陆用户到context
     */
    private Message<?> doWithMessage(Message<?> message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Principal principal = simpMessageHeaderAccessor.getUser();
        if (principal == null) {
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
    public Message<?> beforeHandle(Message<?> message, MessageChannel channel, MessageHandler handler) {
        return message;
    }

    @Override
    public void afterMessageHandled(Message<?> message, MessageChannel channel, MessageHandler handler, Exception ex) {}

    public AuthenticationInterceptor(SessionHelper sessionHelper,
                                     AppProperties appProperties) {
        this.sessionHelper = sessionHelper;
        this.appProperties = appProperties;
    }
}
