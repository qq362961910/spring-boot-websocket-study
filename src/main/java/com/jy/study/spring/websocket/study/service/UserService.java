package com.jy.study.spring.websocket.study.service;

import com.jy.study.spring.websocket.study.entity.User;

public interface UserService {

    User queryUserByUsernameAndPassword(String username, String password);
}
