package com.boot.dao.model;

import com.boot.common.dao.model.BaseModel;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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

    public User(Long id, String name, String email, String phone, Integer type, Integer status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User() {
        super();
    }

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