package com.jy.study.spring.websocket.study.service;

import com.jy.study.spring.websocket.study.entity.User;

import javax.servlet.http.HttpServletResponse;

public interface UserTicketService {

    void bindTicket(User user, HttpServletResponse response);

    User queryUserByTicket(String ticket);

}
