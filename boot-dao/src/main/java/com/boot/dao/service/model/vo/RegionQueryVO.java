package com.boot.dao.service.model.vo;

import com.boot.common.model.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("RegionQueryVO")
public class RegionQueryVO extends BaseVO {

    private Long provinceCode;

    private Long cityCode;

    private Long countyCode;
}
