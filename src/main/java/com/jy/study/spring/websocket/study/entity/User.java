package com.jy.study.spring.websocket.study.entity;

import java.util.ArrayList;
import java.util.List;

public class User {

    /**
     * 用户名
     * */
    private String username;

    /**
     * 密码
     * */
    private String password;

    /**
     * 权限
     * */
    private List<Role> roleList = new ArrayList<>();


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        if(roleList != null && roleList.size() > 0) {
            this.roleList.addAll(roleList);
        }
    }
}
