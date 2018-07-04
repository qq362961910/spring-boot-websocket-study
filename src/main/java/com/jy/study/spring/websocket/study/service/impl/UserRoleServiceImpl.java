package com.jy.study.spring.websocket.study.service.impl;

import com.jy.study.spring.websocket.study.entity.Role;
import com.jy.study.spring.websocket.study.service.UserRoleService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRoleServiceImpl implements UserRoleService, InitializingBean {

    private static final Role ADMIN_ROLE = new Role("admin");
    private static final Role GUEST_ROLE = new Role("guest");

    private static final Map<String, List<Role>> userRoleMap = new HashMap<>();

    @Override
    public List<Role> queryUserRole(String username) {
        return userRoleMap.getOrDefault(username, new ArrayList<>(0));
    }

    @Override
    public void afterPropertiesSet() {
        List<Role> adminRoleList = new ArrayList<>();
        adminRoleList.add(ADMIN_ROLE);
        adminRoleList.add(GUEST_ROLE);
        userRoleMap.put("admin", adminRoleList);
    }
}
