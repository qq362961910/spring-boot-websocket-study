package com.jy.study.spring.websocket.study.service.impl;

import com.jy.study.spring.websocket.study.entity.User;
import com.jy.study.spring.websocket.study.service.UserTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserTicketServiceImpl implements UserTicketService {

    private static final Logger logger = LoggerFactory.getLogger(UserTicketServiceImpl.class);

    private static final short THIRTY_MINUTES = 60 * 30;

    private final HashMap<String, User> userCache = new HashMap<>();

    @Override
    public void bindTicket(User user, HttpServletResponse response) {
        if(user != null) {
            String ticket = UUID.randomUUID().toString().replaceAll("-", "");
            userCache.put(ticket, user);
            Cookie ticketCookie = new Cookie("ticket", ticket);
            ticketCookie.setHttpOnly(true);
            ticketCookie.setMaxAge(THIRTY_MINUTES);
            response.addCookie(ticketCookie);
            logger.info("user: {} bind ticket: {}, expired in {} seconds",user.getUsername(), ticket, ticketCookie.getMaxAge());
        } else {
            logger.error("bind ticket failed with user null");
        }
    }

    @Override
    public User queryUserByTicket(String ticket) {
        User user = null;
        if(ticket != null) {
            for(Map.Entry<String, User> entry: userCache.entrySet()) {
                if(entry.getKey().equals(ticket)) {
                    user = entry.getValue();
                }
            }
        }
        return user;
    }
}
