package com.jy.study.spring.websocket.study.service.impl;

import com.jy.study.spring.websocket.study.service.AsyncTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskServiceImpl.class);

    @Async
    public void sleep10Second() {
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
        logger.info("sleep 5 seconds exit");
    }

}
