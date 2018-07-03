package com.jy.study.spring.websocket.study.config;


import com.jy.study.spring.websocket.study.handler.AppWebSocketAnnotationMethodMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;

@Configuration
public class AppDelegatingWebSocketMessageBrokerConfiguration extends DelegatingWebSocketMessageBrokerConfiguration {

    @Override
    protected SimpAnnotationMethodMessageHandler createAnnotationMethodMessageHandler() {
        return new AppWebSocketAnnotationMethodMessageHandler(
            clientInboundChannel(), clientOutboundChannel(), brokerMessagingTemplate());

    }
}
