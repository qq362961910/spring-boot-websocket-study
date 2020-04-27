package com.jy.study.spring.websocket.study.config;


import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.handler.AuthorityCheckWebSocketAnnotationMethodMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;

@Configuration
public class AppDelegatingWebSocketMessageBrokerConfiguration extends DelegatingWebSocketMessageBrokerConfiguration {

    private AppProperties appProperties;

    @Override
    protected SimpAnnotationMethodMessageHandler createAnnotationMethodMessageHandler() {
        return new AuthorityCheckWebSocketAnnotationMethodMessageHandler(
            clientInboundChannel(), clientOutboundChannel(), brokerMessagingTemplate(), appProperties);
    }

    @Bean
    @Override
    public WebSocketHandler subProtocolWebSocketHandler() {
        return new AppSubProtocolWebSocketHandler(clientInboundChannel(), clientOutboundChannel());
    }

    public AppDelegatingWebSocketMessageBrokerConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    private static class AppSubProtocolWebSocketHandler extends SubProtocolWebSocketHandler{

        private static final Logger logger = LoggerFactory.getLogger(AppDelegatingWebSocketMessageBrokerConfiguration.class);

        AppSubProtocolWebSocketHandler(MessageChannel clientInboundChannel, SubscribableChannel clientOutboundChannel) {
            super(clientInboundChannel, clientOutboundChannel);
        }

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            logger.info("=================> a new connection establish");
            super.afterConnectionEstablished(session);
        }
    }
}


