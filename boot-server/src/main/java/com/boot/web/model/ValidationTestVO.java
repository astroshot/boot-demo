package com.boot.web.model;

import com.boot.common.model.BaseVO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ValidationTestVO extends BaseVO {

    @NotBlank(message = "name 不能为空")
    private String name;

    @NotBlank(message = "phone 不能为空")
    @Length(min = 8, message = "phone 长度至少为 1")
    private String phone;

    @NotBlank(message = "email 不能为空")
    private String email;

    @NotNull(message = "price 不能为空")
    @Min(value = 0, message = "salePrice 不能小于0")
    private Double price;

    @NotNull(message = "name 不能为空")
    @Min(value = 1, message = "number 必须大于0")
    private Integer number;
}
