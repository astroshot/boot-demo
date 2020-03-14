package com.boot.web.controller;

import com.boot.common.model.JSONResult;
import com.boot.common.web.controller.BaseController;
import com.boot.dao.service.RegionService;
import com.boot.dao.service.model.vo.RegionQueryVO;
import com.boot.dao.service.model.vo.RegionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController {

    @Resource
    protected RegionService regionService;

    @GetMapping("/query")
    public JSONResult<?> query(RegionQueryVO param) {
        if (param == null) {
            return JSONResult.success();
        }

        List<RegionVO> regionVOS = regionService.query(param);
        return JSONResult.success(regionVOS);
    }

    @GetMapping("/provinces")
    public JSONResult<?> getProvinces() {
        List<RegionVO> regionVOS = regionService.getProvinces();
        return JSONResult.success(regionVOS);
    }
}
