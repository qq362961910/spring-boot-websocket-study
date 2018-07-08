package com.jy.study.spring.websocket.study.helper;

import java.util.HashMap;
import java.util.Map;

public class SessionHelper {

    private final Map<String, SessionConfig> sessionConfigMap = new HashMap<>();


    public void setSessionP2pErrorSimpSubscriptionId(String sessionId, String p2pErrorSimpSubscriptionId) {
        SessionConfig sessionConfig = getUserSessionConfig(sessionId, true);
        sessionConfig.setP2pErrorSimpSubscriptionId(p2pErrorSimpSubscriptionId);
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

        private String p2pErrorSimpSubscriptionId;

        public String getP2pErrorSimpSubscriptionId() {
            return p2pErrorSimpSubscriptionId;
        }

        public void setP2pErrorSimpSubscriptionId(String p2pErrorSimpSubscriptionId) {
            this.p2pErrorSimpSubscriptionId = p2pErrorSimpSubscriptionId;
        }
    }
}
