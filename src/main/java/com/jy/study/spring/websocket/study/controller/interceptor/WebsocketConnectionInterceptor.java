package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.Cookie;
import java.util.Map;

public class WebsocketConnectionInterceptor implements HandshakeInterceptor {

    private AppProperties appProperties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if(request instanceof ServletServerHttpRequest) {
            Cookie[] cookies = ((ServletServerHttpRequest)request).getServletRequest().getCookies();
            if(cookies != null && cookies.length > 0) {
                for(Cookie cookie: cookies) {
                    if(appProperties.getTicketKey().equals(cookie.getName())) {
                        attributes.put("ticket", cookie.getValue());
                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
