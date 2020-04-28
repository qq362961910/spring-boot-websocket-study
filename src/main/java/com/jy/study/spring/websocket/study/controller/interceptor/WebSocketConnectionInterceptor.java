package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import org.slf4j.MDC;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class WebSocketConnectionInterceptor implements HandshakeInterceptor {

    private static final String TRACE_ID_HEADER_NAME = "X-Request-Id";
    private static final String TRACE_ID_LOG_NAME = "TRACE_ID";

    private AppProperties appProperties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if(true) {
            return false;
        }
        if(request instanceof HttpServletRequest) {
            String traceId = ((HttpServletRequest)request).getHeader(TRACE_ID_HEADER_NAME);
            if(StringUtils.isEmpty(traceId)) {
                //todo generate traceId
            } else {
                MDC.put(TRACE_ID_LOG_NAME, traceId);
            }
        }
        if(request instanceof ServletServerHttpRequest) {
            Cookie[] cookies = ((ServletServerHttpRequest)request).getServletRequest().getCookies();
            if(cookies != null && cookies.length > 0) {
                for(Cookie cookie: cookies) {
                    if(appProperties.getTicketKey().equals(cookie.getName())) {
                        attributes.put(appProperties.getTicketKey(), cookie.getValue());
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    public WebSocketConnectionInterceptor(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
}
