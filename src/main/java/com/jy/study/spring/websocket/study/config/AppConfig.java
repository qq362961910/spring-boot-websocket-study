package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.controller.interceptor.AuthenticationInterceptor;
import com.jy.study.spring.websocket.study.controller.interceptor.ConnectionInterceptor;
import com.jy.study.spring.websocket.study.controller.interceptor.WebsocketConnectionInterceptor;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.listener.WebSocketConnectionStateListener;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SecurityHelper securityHelper() {
        return new SecurityHelper();
    }
    @Bean
    public WebsocketConnectionInterceptor websocketConnectionInterceptor() {
        return new WebsocketConnectionInterceptor();
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor(UserTicketService userTicketService, SecurityHelper securityHelper) {
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();
        authenticationInterceptor.setUserTicketService(userTicketService);
        authenticationInterceptor.setSecurityHelper(securityHelper);
        return authenticationInterceptor;
    }
    @Bean
    public ConnectionInterceptor connectionInterceptor() {
        return new ConnectionInterceptor();
    }

    @Bean
    public WebSocketConnectionStateListener webSocketConnectionStateListener() {
        return new WebSocketConnectionStateListener();
    }
}
