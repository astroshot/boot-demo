package com.boot.dao.service.model;

import lombok.Data;

@Data
public class UserBO {

    private String name;

    private String email;

    private String phone;

    private Integer status;

    private String description;

}
