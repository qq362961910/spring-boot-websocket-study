package com.jy.study.spring.websocket.study.helper;

import java.util.HashMap;
import java.util.Map;

public class SessionHelper {

    private final Map<String, SessionConfig> sessionConfigMap = new HashMap<>();


    public void setSessionP2pSimpSubscriptionId(String sessionId, String p2pSimpSubscriptionId) {
        SessionConfig sessionConfig = getUserSessionConfig(sessionId, true);
        sessionConfig.setP2pSimpSubscriptionId(p2pSimpSubscriptionId);
    }

    public synchronized SessionConfig getUserSessionConfig(String sessionId, boolean autoCreate) {
        SessionConfig sessionConfig = sessionConfigMap.get(sessionId);
        if(sessionConfig == null && autoCreate) {
            sessionConfig = new SessionConfig();
            sessionConfigMap.put(sessionId, sessionConfig);
        }
        return sessionConfig;
    }



    public static class SessionConfig {

        private String p2pSimpSubscriptionId;

        public String getP2pSimpSubscriptionId() {
            return p2pSimpSubscriptionId;
        }

        public void setP2pSimpSubscriptionId(String p2pSimpSubscriptionId) {
            this.p2pSimpSubscriptionId = p2pSimpSubscriptionId;
        }
    }
}
