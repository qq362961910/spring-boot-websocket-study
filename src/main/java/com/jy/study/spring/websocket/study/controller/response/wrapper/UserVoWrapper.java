package com.jy.study.spring.websocket.study.controller.response.wrapper;

import com.jy.study.spring.websocket.study.controller.response.vo.UserVo;
import com.jy.study.spring.websocket.study.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserVoWrapper {

    public UserVo buildUserVo(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setUsername(user.getUsername());
        return userVo;
    }

}
