package com.jy.study.spring.websocket.study.controller.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
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
        SimpAttributes attributes = SimpAttributesContextHolder.getAttributes();
        if(attributes != null) {
            logger.info("\r\nuser: {}\r\nclass-method: {}\r\nargs: {}",
                attributes.getSessionId(),
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                joinPoint.getArgs());
        }
    }

    @After("log()")
    public void doAfter() { }

    @AfterReturning(pointcut = "log()", returning = "obj")
    public void afterReturning(Object obj) throws JsonProcessingException {
        logger.info("response: {}", objectMapper.writeValueAsString(obj));
    }

    public ControllerAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
