package com.boot.dao.service.model;

import com.boot.common.dao.model.BaseModel;
import lombok.Data;

@Data
public class UserBO extends BaseModel {

    private String name;

    private String email;

    private String phone;

    private Integer status;

    private String description;

    private Integer type;

}
