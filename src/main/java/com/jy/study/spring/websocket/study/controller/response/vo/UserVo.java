package com.jy.study.spring.websocket.study.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo extends BaseVo {

    private static final String STRUCTURE_TYPE = "user";

    /**
     * 用户名
     */
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserVo() {
        super(STRUCTURE_TYPE);
    }
}
