package com.jy.study.spring.websocket.study.service.impl;

import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        User user = null;
        if("admin".equals(username) && "admin".equals(password)) {
            user = new User();
            user.setUsername(username);
        }
        return user;
    }
}
