package com.boot.web.model;

import com.boot.common.model.BasePOJO;
import lombok.Data;

@Data
public class JacksonTestVO extends BasePOJO {

    private String name;

    private int count;

}
