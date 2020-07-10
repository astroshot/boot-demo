package com.boot.web.controller;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.controller.BaseController;
import com.boot.dao.service.RegionService;
import com.boot.dao.service.model.vo.RegionQueryVO;
import com.boot.dao.service.model.vo.RegionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
@Api(value = "地区查询")
public class AddressController extends BaseController {

    @Resource
    protected RegionService regionService;

    @ApiOperation(value = "层级区域查询", notes = "按层查询行政区域", tags = {"区域查询"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "RegionQueryVo", name = "param", value = "请求参数", required = true)
    })
    @GetMapping("/query")
    public JSONResponse<List<RegionVO>> query(@Valid RegionQueryVO param) {
        if (param == null) {
            return JSONResponse.success();
        }

        List<RegionVO> regionVOS = regionService.query(param);
        return JSONResponse.success(regionVOS);
    }

    @GetMapping("/provinces")
    public JSONResponse<List<RegionVO>> getProvinces() {
        List<RegionVO> regionVOS = regionService.getProvinces();
        return JSONResponse.success(regionVOS);
    }
}
