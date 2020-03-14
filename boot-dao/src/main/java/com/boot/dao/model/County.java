package com.boot.dao.model;

import com.boot.common.dao.model.BaseModel;
import java.util.Date;

public class County extends BaseModel {
    private Long cityCode;

    private Long code;

    private String name;

    private Date createTime;

    private Date updateTime;

    public County(Long id, Long cityCode, Long code, String name, Date createTime, Date updateTime) {
        this.id = id;
        this.cityCode = cityCode;
        this.code = code;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public County() {
        super();
    }

    public Long getCityCode() {
        return cityCode;
    }

    public void setCityCode(Long cityCode) {
        this.cityCode = cityCode;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}