package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.controller.interceptor.AuthenticationInterceptor;
import com.jy.study.spring.websocket.study.controller.interceptor.ConnectionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    @Bean
    public ConnectionInterceptor connectionInterceptor() {
        return new ConnectionInterceptor();
    }
}
