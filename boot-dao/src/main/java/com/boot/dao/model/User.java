package com.boot.dao.model;

import com.boot.common.dao.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@Getter
@AllArgsConstructor
@ApiModel
public class User extends BaseModel {
    @ApiModelProperty(value = "user name")
    private String name;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户类型，1 顾客 2 卖主")
    private Integer type;

    @ApiModelProperty(value = "0-created 1-normal 2-deleted")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @AllArgsConstructor
    public enum COLUMNS {
        ID("id"),
        NAME("name"),
        EMAIL("email"),
        PHONE("phone"),
        TYPE("type"),
        STATUS("status"),
        CREATED_AT("created_at"),
        UPDATED_AT("updated_at");

        @Getter
        private final String column;
    }
}