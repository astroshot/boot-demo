package com.boot.dao.model;

import com.boot.common.dao.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Data
@Builder
public class User extends BaseModel {
    private String name;

    private String email;

    private String phone;

    private Integer type;

    private Integer status;

    private Date createdAt;

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