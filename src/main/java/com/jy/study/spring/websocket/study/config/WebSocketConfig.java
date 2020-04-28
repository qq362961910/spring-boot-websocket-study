package com.jy.study.spring.websocket.study.config;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.controller.interceptor.WebSocketConnectionInterceptor;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.handler.AppStompErrorHandler;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private AppProperties appProperties;
    private WebSocketConnectionInterceptor websocketConnectionInterceptor;
    private AppStompErrorHandler appStompErrorHandler;
    private UserTicketService userTicketService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        //激活一个简单的基于内存的消息代理,客户端使用订阅方法时添加的前缀
        //心跳第一个参数表示服务端向客户端写出心跳的频率(0表示服务器不发送心跳)
        //心跳第二个参数表示客户端多久应该向服务器发送心跳, (0表示客户端不应该发送心跳)
        //服务端取两个心跳值较小的作为发心跳的频率，在stomp.js实现中使用两个值中较大者作为发送心跳的频率(在stomp1.1的协议中client默认值为10秒(10000),所以当服务器响应的值大于0或者小于10时不生效,只能修改客户端实现)
//        config.enableSimpleBroker(appProperties.getDestinationPrefix()).setHeartbeatValue(new long[]{appProperties.getServerHeartBeatFrequency(), appProperties.getClientHeartBeatFrequency()}).setTaskScheduler(taskScheduler);
        config.enableSimpleBroker(appProperties.getDestinationPrefix());
        //客户端请求服务端使用@MessageMapping注解方法时添加的前缀
        config.setApplicationDestinationPrefixes(appProperties.getApplicationDestinationPrefix());
        //指定点对点消息前缀, 每个用户独立拥有一个
        //当订阅/user/queue/position-updates时会被转换成/queue/position-updatesi9oqdfzo
        //当发送消息到/user/{username}/queue/position-updates, 会被转换成/queue/position-updatesi9oqdfzo
        config.setUserDestinationPrefix(appProperties.getUserDestinationPrefix());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(appStompErrorHandler)
                .addEndpoint(appProperties.getEndPoint()).setHandshakeHandler(new AppEndpointHandShakeHandler())
                .setAllowedOrigins(appProperties.getAllowedOrigin())
                .withSockJS()
                .setInterceptors(websocketConnectionInterceptor);
    }

    /**
     * 配置客户端入站通道拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {}


    public WebSocketConfig(WebSocketConnectionInterceptor websocketConnectionInterceptor,
                           AppStompErrorHandler appStompErrorHandler,
                           AppProperties appProperties,
                           UserTicketService userTicketService) {
        this.websocketConnectionInterceptor = websocketConnectionInterceptor;
        this.appStompErrorHandler = appStompErrorHandler;
        this.appProperties = appProperties;
        this.userTicketService = userTicketService;
    }

    private class AppEndpointHandShakeHandler extends DefaultHandshakeHandler {
        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            String ticket = (String)attributes.get(appProperties.getTicketKey());
            if(StringUtils.isEmpty(ticket)) {
                return null;
            } else {
                User user = userTicketService.queryUserByTicket(ticket);
                if(user == null) {
                    return null;
                } else {
                    return user::getUsername;
                }
            }
        }
    }
}



