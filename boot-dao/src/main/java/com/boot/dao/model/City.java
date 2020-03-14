package com.boot.dao.model;

import com.boot.common.dao.model.BaseModel;
import java.util.Date;

public class City extends BaseModel {
    private Long provinceCode;

    private Long code;

    private String name;

    private Date createTime;

    private Date updateTime;

    public City(Long id, Long provinceCode, Long code, String name, Date createTime, Date updateTime) {
        this.id = id;
        this.provinceCode = provinceCode;
        this.code = code;
        this.name = name;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public City() {
        super();
    }

    public Long getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Long provinceCode) {
        this.provinceCode = provinceCode;
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