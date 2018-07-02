package com.jy.study.spring.websocket.study.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.websocket.session")
public class AppProperties {

    private String p2pSimpSubscriptionId = "/topic/p2p";

    public String getP2pSimpSubscriptionId() {
        return p2pSimpSubscriptionId;
    }

    public void setP2pSimpSubscriptionId(String p2pSimpSubscriptionId) {
        this.p2pSimpSubscriptionId = p2pSimpSubscriptionId;
    }
}
