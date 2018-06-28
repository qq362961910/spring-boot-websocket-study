package com.jy.study.spring.websocket.study.controller.response.wrapper;

import com.jy.study.spring.websocket.study.controller.response.vo.ResultVo;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResultVoWrapper {

    private static final String FAIL = "500";

    private static final String SUCCESS = "200";

    /**
     * 成功
     */
    public ResultVo buildSuccess() {
        return buildSuccess(null);
    }

    public ResultVo buildSuccess(Map<String, Object> data) {
        ResultVo vo = new ResultVo();
        vo.setCode(SUCCESS);
        vo.setData(data);
        return vo;
    }

    /**
     * 失败
     */
    public ResultVo buildFail() {
        return buildFail(FAIL, null);
    }

    public ResultVo buildFail(String code) {
        return buildFail(code, null);
    }

    public ResultVo buildFail(String code, Map<String, Object> data) {
        ResultVo vo = new ResultVo();
        vo.setCode(code);
        vo.setData(data);
        return vo;
    }

}
