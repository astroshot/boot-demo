package com.boot.web.model;
import lombok.Data;

@Data
public class BookVO extends BaseVO {
    private long id;
    private String name;
    private String description;
    private String isbn;
}
