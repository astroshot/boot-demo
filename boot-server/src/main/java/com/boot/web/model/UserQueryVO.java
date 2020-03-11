package com.boot.web.model;

import com.boot.common.model.BaseVO;
import lombok.Data;


@Data
public class UserQueryVO extends BaseVO {
    private String email;
    private String name;
    private int pageNo;
    private int pageSize;
}
