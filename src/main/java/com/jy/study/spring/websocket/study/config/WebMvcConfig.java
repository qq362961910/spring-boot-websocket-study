package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.controller.interceptor.HttpInterceptor;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor());
    }

    /**
     * 返回值要写接口类型
     * */
    @Bean
    public HandlerInterceptor httpInterceptor() {
        return new HttpInterceptor();
    }
}
