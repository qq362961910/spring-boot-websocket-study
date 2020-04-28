package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.config.properties.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息控制器
 *
 * @author yj
 * @since 2020-04-28 10:00
 **/
@RequestMapping("sendMessage")
@RestController
public class SendMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SendMessageController.class);

    private AppProperties appProperties;
    private SimpUserRegistry simpUserRegistry;
    private SimpMessageSendingOperations brokerTemplate;

    @GetMapping("toUser")
    public void sendUserMessage(String username, String message) {
        SimpUser simpUser = simpUserRegistry.getUser(username);
        if(simpUser != null) {
            this.brokerTemplate.convertAndSendToUser(simpUser.getName(), appProperties.getUserTopic().replace(appProperties.getUserDestinationPrefix(), ""), message);
        } else {
            logger.warn("用户: {}不在线， 忽略发送消息", username);
        }
    }

    public SendMessageController(AppProperties appProperties, SimpUserRegistry simpUserRegistry, SimpMessageSendingOperations brokerTemplate) {
        this.appProperties = appProperties;
        this.simpUserRegistry = simpUserRegistry;
        this.brokerTemplate = brokerTemplate;
    }
}
