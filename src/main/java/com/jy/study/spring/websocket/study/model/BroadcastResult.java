package com.jy.study.spring.websocket.study.model;

public class BroadcastResult {

    private String msg;

    public String getMsg() {
        return "broadcast: " + msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
