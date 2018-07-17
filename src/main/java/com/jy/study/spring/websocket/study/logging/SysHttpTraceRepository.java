package com.jy.study.spring.websocket.study.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class SysHttpTraceRepository extends InMemoryHttpTraceRepository {

    private static final Logger logger = LoggerFactory.getLogger(SysHttpTraceRepository.class);

    /**
     * 存储请求日志
     * */
    @Override
    public void add(HttpTrace trace) {
        logger.info("http trace: uri: {}, time taken: {}", trace.getRequest().getUri(), trace.getTimeTaken());
        super.add(trace);
    }
}
