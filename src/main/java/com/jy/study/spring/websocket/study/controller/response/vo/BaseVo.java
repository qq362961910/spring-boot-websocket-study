package com.jy.study.spring.websocket.study.controller.response.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseVo {

    /**
     * 结构类型
     */
    @JsonProperty("structureType")
    protected String structureType;

    public String getStructureType() {
        return structureType;
    }

    public void setStructureType(String structureType) {
        this.structureType = structureType;
    }

    public BaseVo(String structureType) {
        this.structureType = structureType;
    }
}
