package com.boot.web.model;

import com.boot.common.model.BaseVO;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class UserVO extends BaseVO {

    @NotBlank(message = "name 不能为空")
    private String name;

    private String email;

    private String phone;

    private String description;

}
