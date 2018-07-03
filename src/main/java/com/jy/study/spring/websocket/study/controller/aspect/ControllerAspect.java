package com.jy.study.spring.websocket.study.controller.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {

    private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    private ObjectMapper objectMapper;

    @Pointcut("execution(public * com.jy.study.spring.websocket.study.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        System.out.println("hahaha");
    }

    @After("log()")
    public void doAfter() {
        System.out.println("hahaha");
    }

    @AfterReturning(pointcut = "log()", returning = "obj")
    public void afterReturning(Object obj) {
        System.out.println("hahaha");
    }

    public ControllerAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
