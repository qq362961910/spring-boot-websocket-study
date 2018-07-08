package com.jy.study.spring.websocket.study.config;


import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.handler.AuthorityCheckWebSocketAnnotationMethodMessageHandler;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.annotation.support.SimpAnnotationMethodMessageHandler;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;

@Configuration
public class AppDelegatingWebSocketMessageBrokerConfiguration extends DelegatingWebSocketMessageBrokerConfiguration {

    private SecurityHelper securityHelper;
    private AppProperties appProperties;

    @Override
    protected SimpAnnotationMethodMessageHandler createAnnotationMethodMessageHandler() {
        return new AuthorityCheckWebSocketAnnotationMethodMessageHandler(
            clientInboundChannel(), clientOutboundChannel(), brokerMessagingTemplate(), securityHelper, appProperties);

    }

    public AppDelegatingWebSocketMessageBrokerConfiguration(SecurityHelper securityHelper,
                                                            AppProperties appProperties) {
        this.securityHelper = securityHelper;
        this.appProperties = appProperties;
    }
}
