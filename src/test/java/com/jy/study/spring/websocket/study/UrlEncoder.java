package com.jy.study.spring.websocket.study;

public class UrlEncoder {

    public static void main(String[] args) throws Exception {
        System.out.println(java.net.URLEncoder.encode("rtsp://192.168.114.121/11", "UTF-8"));
    }
}
