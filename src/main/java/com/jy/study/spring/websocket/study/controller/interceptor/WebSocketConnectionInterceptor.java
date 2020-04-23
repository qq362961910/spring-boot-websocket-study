package com.jy.study.spring.websocket.study.controller.interceptor;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import com.jy.study.spring.websocket.study.service.UserTicketService;
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

    public static final String TRACE_ID_HEADER_NAME = "X-Request-Id";
    public static final String TRACE_ID_LOG_NAME = "TRACE_ID";

    private UserTicketService userTicketService;
    private UserRoleService userRoleService;
    private AppProperties appProperties;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
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
                        User user = userTicketService.queryUserByTicket(cookie.getValue());
                        if(user != null) {
                            user.setRoleList(userRoleService.queryUserRoleName(user.getUsername()));
                            attributes.put("user", user);
                        }
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

    public WebSocketConnectionInterceptor(UserTicketService userTicketService, UserRoleService userRoleService, AppProperties appProperties) {
        this.userTicketService = userTicketService;
        this.userRoleService = userRoleService;
        this.appProperties = appProperties;
    }
}
