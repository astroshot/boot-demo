package com.boot.web.model;

import com.boot.common.web.model.BaseVO;
import lombok.Data;


@Data
public class UserVO extends BaseVO {

    private String name;

    private String email;

    private String phone;

    private String description;

}
