package com.boot.dao.service;

import com.boot.dao.service.model.vo.RegionQueryVO;
import com.boot.dao.service.model.vo.RegionVO;

import java.util.List;

public interface RegionService {

    List<RegionVO> getProvinces();

    List<RegionVO> query(RegionQueryVO condition);

}
