package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.controller.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //激活一个简单的基于内存的消息代理,客户端使用订阅方法时添加的前缀
        config.enableSimpleBroker("/topic");
        //客户端请求服务端使用@MessageMapping注解方法时添加的前缀
        config.setApplicationDestinationPrefixes("/app");
        //指定点对点消息前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置客户端入站通道拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authenticationInterceptor);
    }

    public WebSocketConfig(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }
}
