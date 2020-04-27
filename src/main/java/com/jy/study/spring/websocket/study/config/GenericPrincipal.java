package com.jy.study.spring.websocket.study.config;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.List;

/**
 * 通用Principal
 *
 * @author yj
 * @since 2020-04-23 20:09
 **/
public class GenericPrincipal implements Principal {

    private final String username;

    private final List<String> roles;

    @Override
    public String getName() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public GenericPrincipal(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
    }
}
