package com.jy.study.spring.websocket.study.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel(value = "响应")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo {

    @ApiModelProperty(value = "响应码", example = "200")
    @JsonProperty("errorCode")
    private String code;

    @ApiModelProperty(value = "响应消息", example = "成功")
    @JsonProperty("message")
    private String message;

    @ApiModelProperty(value = "数据", example = "{count: 1}")
    @JsonProperty("data")
    private Map<String, Object> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
