package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.controller.interceptor.AuthenticationInterceptor;
import com.jy.study.spring.websocket.study.controller.interceptor.WebsocketConnectionInterceptor;
import com.jy.study.spring.websocket.study.handler.AppStompErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private AppProperties appProperties;
    private AuthenticationInterceptor authenticationInterceptor;
    private WebsocketConnectionInterceptor websocketConnectionInterceptor;
    private AppStompErrorHandler appStompErrorHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //激活一个简单的基于内存的消息代理,客户端使用订阅方法时添加的前缀
        config.enableSimpleBroker(appProperties.getDestinationPrefix());
        //客户端请求服务端使用@MessageMapping注解方法时添加的前缀
        config.setApplicationDestinationPrefixes(appProperties.getApplicationDestinationPrefix());
        //指定点对点消息前缀
        config.setUserDestinationPrefix(appProperties.getUserDestinationPrefix());

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(appStompErrorHandler)
                .addEndpoint(appProperties.getEndPoint()).setAllowedOrigins(appProperties.getAllowedOrigin()).withSockJS()
                .setInterceptors(websocketConnectionInterceptor);
    }

    /**
     * 配置客户端入站通道拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authenticationInterceptor);
    }


    public WebSocketConfig(AuthenticationInterceptor authenticationInterceptor,
                           WebsocketConnectionInterceptor websocketConnectionInterceptor,
                           AppStompErrorHandler appStompErrorHandler,
                           AppProperties appProperties) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.websocketConnectionInterceptor = websocketConnectionInterceptor;
        this.appStompErrorHandler = appStompErrorHandler;
        this.appProperties = appProperties;
    }
}
