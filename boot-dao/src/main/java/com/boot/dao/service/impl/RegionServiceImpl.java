package com.boot.dao.service.impl;

import com.boot.dao.mapper.CityMapper;
import com.boot.dao.mapper.CountyMapper;
import com.boot.dao.mapper.ProvinceMapper;
import com.boot.dao.mapper.TownMapper;
import com.boot.dao.model.*;
import com.boot.dao.service.RegionService;
import com.boot.dao.service.model.vo.RegionQueryVO;
import com.boot.dao.service.model.vo.RegionVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class RegionServiceImpl implements RegionService {

    @Resource
    protected ProvinceMapper provinceMapper;

    @Resource
    protected CityMapper cityMapper;

    @Resource
    protected CountyMapper countyMapper;

    @Resource
    protected TownMapper townMapper;

    protected RegionVO convertFrom(Province record) {
        if (record == null) {
            return null;
        }

        RegionVO vo = new RegionVO();
        vo.setCode(record.getCode());
        vo.setName(record.getName());
        return vo;
    }

    protected List<RegionVO> convertFromProvinces(List<Province> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        List<RegionVO> vos = new ArrayList<>(records.size());
        records.forEach(item -> {
            RegionVO vo = convertFrom(item);
            vos.add(vo);
        });
        return vos;
    }

    protected RegionVO convertFrom(City record) {
        if (record == null) {
            return null;
        }

        RegionVO vo = new RegionVO();
        vo.setCode(record.getCode());
        vo.setName(record.getName());
        return vo;
    }

    protected List<RegionVO> convertFromCities(List<City> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        List<RegionVO> vos = new ArrayList<>(records.size());
        records.forEach(item -> {
            RegionVO vo = convertFrom(item);
            vos.add(vo);
        });
        return vos;
    }

    protected RegionVO convertFrom(County record) {
        if (record == null) {
            return null;
        }

        RegionVO vo = new RegionVO();
        vo.setCode(record.getCode());
        vo.setName(record.getName());
        return vo;
    }

    protected List<RegionVO> convertFromCounties(List<County> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        List<RegionVO> vos = new ArrayList<>(records.size());
        records.forEach(item -> {
            RegionVO vo = convertFrom(item);
            vos.add(vo);
        });
        return vos;
    }

    protected RegionVO convertFrom(Town record) {
        if (record == null) {
            return null;
        }

        RegionVO vo = new RegionVO();
        vo.setCode(record.getCode());
        vo.setName(record.getName());
        return vo;
    }

    protected List<RegionVO> convertFromTowns(List<Town> records) {
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>(0);
        }

        List<RegionVO> vos = new ArrayList<>(records.size());
        records.forEach(item -> {
            RegionVO vo = convertFrom(item);
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public List<RegionVO> getProvinces() {
        ProvinceExample example = new ProvinceExample();
        List<Province> provinces = provinceMapper.selectByExample(example);

        return convertFromProvinces(provinces);
    }

    @Override
    public List<RegionVO> query(RegionQueryVO condition) {
        Assert.notNull(condition, "condition cannot be null");

        if (condition.getProvinceCode() != null) {
            CityExample example = new CityExample();
            example.createCriteria().andProvinceCodeEqualTo(condition.getProvinceCode());
            List<City> cities = cityMapper.selectByExample(example);
            return convertFromCities(cities);
        }

        if (condition.getCityCode() != null) {
            CountyExample example = new CountyExample();
            example.createCriteria().andCityCodeEqualTo(condition.getCityCode());
            List<County> counties = countyMapper.selectByExample(example);
            return convertFromCounties(counties);
        }

        if (condition.getCountyCode() != null) {
            TownExample example = new TownExample();
            example.createCriteria().andCountyCodeEqualTo(condition.getCountyCode());
            List<Town> towns = townMapper.selectByExample(example);
            return convertFromTowns(towns);
        }

        return new ArrayList<>(0);
    }
}
