package com.jy.study.spring.websocket.study.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.websocket.session")
public class AppProperties {

    private String destinationPrefix = "/topic";

    private String userDestinationPrefix = "/user";

    private String applicationDestinationPrefix = "/app";

    private String p2p = "/p2p";

    public String getDestinationPrefix() {
        return destinationPrefix;
    }

    public void setDestinationPrefix(String destinationPrefix) {
        this.destinationPrefix = destinationPrefix;
    }

    public String getUserDestinationPrefix() {
        return userDestinationPrefix;
    }

    public void setUserDestinationPrefix(String userDestinationPrefix) {
        this.userDestinationPrefix = userDestinationPrefix;
    }

    public String getApplicationDestinationPrefix() {
        return applicationDestinationPrefix;
    }

    public void setApplicationDestinationPrefix(String applicationDestinationPrefix) {
        this.applicationDestinationPrefix = applicationDestinationPrefix;
    }

    public String getP2p() {
        return p2p;
    }

    public void setP2p(String p2p) {
        this.p2p = p2p;
    }
}
