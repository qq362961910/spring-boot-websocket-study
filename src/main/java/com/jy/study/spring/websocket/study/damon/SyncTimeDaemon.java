package com.jy.study.spring.websocket.study.damon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class SyncTimeDaemon implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(SyncTimeDaemon.class);

    private boolean stop = false;

    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void run(String... args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (!stop) {
            simpMessagingTemplate.convertAndSend("/topic/sync/time", simpleDateFormat.format(new Date()));
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        stop = true;
    }

    public SyncTimeDaemon(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
