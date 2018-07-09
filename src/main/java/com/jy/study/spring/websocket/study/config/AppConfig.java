package com.jy.study.spring.websocket.study.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.controller.interceptor.AuthenticationInterceptor;
import com.jy.study.spring.websocket.study.controller.interceptor.WebSocketConnectionInterceptor;
import com.jy.study.spring.websocket.study.handler.AppStompErrorHandler;
import com.jy.study.spring.websocket.study.helper.SecurityHelper;
import com.jy.study.spring.websocket.study.helper.SessionHelper;
import com.jy.study.spring.websocket.study.listener.WebSocketConnectionStateListener;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({AppProperties.class})
@Configuration
public class AppConfig {

    @Bean
    public SecurityHelper securityHelper() {
        return new SecurityHelper();
    }
    @Bean
    public WebSocketConnectionInterceptor websocketConnectionInterceptor(AppProperties appProperties) {
        return new WebSocketConnectionInterceptor(appProperties);
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor(UserTicketService userTicketService,
                                                               UserRoleService userRoleService,
                                                               SecurityHelper securityHelper,
                                                               SessionHelper sessionHelper,
                                                               AppProperties appProperties
                                                               ) {
        return new AuthenticationInterceptor(userTicketService, userRoleService, securityHelper, sessionHelper, appProperties);
    }

    @Bean
    public WebSocketConnectionStateListener webSocketConnectionStateListener() {
        return new WebSocketConnectionStateListener();
    }

    @Bean
    public AppStompErrorHandler appStompErrorHandler(SessionHelper sessionHelper, AppProperties appProperties) {
        return new AppStompErrorHandler(sessionHelper, appProperties);
    }

    @Bean
    public SessionHelper sessionHelper() {
        return new SessionHelper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
