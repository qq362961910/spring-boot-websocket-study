package com.jy.study.spring.websocket.study.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysHttpTraceRepository implements HttpTraceRepository {

    private static final Logger logger = LoggerFactory.getLogger(SysHttpTraceRepository.class);

    /**
     * 查询所有请求日志
     * */
    @Override
    public List<HttpTrace> findAll() {
        return null;
    }

    /**
     * 存储请求日志
     * */
    @Override
    public void add(HttpTrace trace) {
        logger.info("http trace: uri: {}, time taken: {}", trace.getRequest().getUri(), trace.getTimeTaken());
    }
}
