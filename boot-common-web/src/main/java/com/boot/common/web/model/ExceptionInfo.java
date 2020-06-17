package com.boot.common.web.model;

import com.boot.common.model.BaseVO;
import lombok.Data;

@Data
public class ExceptionInfo extends BaseVO {

    protected String parameterName;

    protected String info;

}
