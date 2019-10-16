package com.boot.web.model;

import lombok.Data;

@Data
public class UserQueryVO {
    private String email;
    private String name;
    private int pageNo;
    private int pageSize;
}
