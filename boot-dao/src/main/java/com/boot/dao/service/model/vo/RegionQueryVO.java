package com.boot.dao.service.model.vo;

import com.boot.common.model.BaseVO;
import lombok.Data;

@Data
public class RegionQueryVO extends BaseVO {

    private Long provinceCode;

    private Long cityCode;

    private Long countyCode;
}
