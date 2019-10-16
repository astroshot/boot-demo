package com.boot.dao.service.model;

import lombok.Data;

@Data
public class UserQueryBO {
    private String name;
    private String email;
    private Integer pageNo;
    private Integer pageSize;
}
