package com.jy.study.spring.websocket.study.controller;

import com.jy.study.spring.websocket.study.controller.response.vo.UserVo;
import com.jy.study.spring.websocket.study.controller.response.wrapper.ResultVoWrapper;
import com.jy.study.spring.websocket.study.controller.response.wrapper.UserVoWrapper;
import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.service.UserLogicErrorCode;
import com.jy.study.spring.websocket.study.service.UserService;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    private UserTicketService userTicketService;
    private UserVoWrapper userVoWrapper;
    private ResultVoWrapper resultVoWrapper;

    @PostMapping("login")
    public Object login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
        User user = userService.queryUserByUsernameAndPassword(username, password);
        if(user == null) {
            logger.info("user login failed with username: {}, password: {} at {}", username, password, new Date());
            return resultVoWrapper.buildFail(UserLogicErrorCode.USERNAME_OR_PASSWORD_ERROR);
        }
        userTicketService.bindTicket(user, response);
        UserVo userVo = userVoWrapper.buildUserVo(user);
        Map<String, Object> data = new HashMap<>();
        data.put("user", userVo);
        logger.info("user: {} login success, now: {}", user.getUsername(), new Date());
        return resultVoWrapper.buildSuccess(data);
    }


    public UserController(UserService userService, UserTicketService userTicketService, UserVoWrapper userVoWrapper, ResultVoWrapper resultVoWrapper) {
        this.userService = userService;
        this.userTicketService = userTicketService;
        this.userVoWrapper = userVoWrapper;
        this.resultVoWrapper = resultVoWrapper;
    }
}
