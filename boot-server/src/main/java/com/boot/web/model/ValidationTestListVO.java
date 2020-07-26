package com.boot.web.model;

import com.boot.common.model.BaseVO;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class ValidationTestListVO extends BaseVO {

    @Valid
    List<ValidationTestVO> data;
}
