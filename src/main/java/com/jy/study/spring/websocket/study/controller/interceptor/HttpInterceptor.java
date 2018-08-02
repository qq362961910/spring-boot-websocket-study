package com.jy.study.spring.websocket.study.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HttpInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        logger.info("===> HttpInterceptor#preHandle() runs, uri: {}", request.getRequestURI());
        return true;
    }

    /**
     * handler报异常后不会执行
     * */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("===> HttpInterceptor#postHandle() runs, uri: {}", request.getRequestURI());
    }

    /**
     * handler 报异常后依然执行
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("===> HttpInterceptor#afterCompletion() runs, uri: {}", request.getRequestURI());
    }
}
