package com.jy.study.spring.websocket.study.service;

import com.jy.study.spring.websocket.study.entity.Role;

import java.util.List;

public interface UserRoleService {

    List<Role> queryUserRole(String username);
}
