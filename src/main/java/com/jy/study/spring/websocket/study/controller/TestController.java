package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.service.AsyncTaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
public class TestController {

    private AsyncTaskService asyncTaskService;

    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("asyn_task")
    public String asyncTask() {
        asyncTaskService.sleep10Second();
        return "success";
    }

    public TestController(AsyncTaskService asyncTaskService) {
        this.asyncTaskService = asyncTaskService;
    }
}
