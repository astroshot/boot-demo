package com.boot.dao.mapper;

import com.boot.dao.model.County;
import com.boot.dao.model.CountyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CountyMapper {
    int countByExample(CountyExample example);

    int deleteByExample(CountyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(County record);

    int insertSelective(County record);

    List<County> selectByExample(CountyExample example);

    County selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") County record, @Param("example") CountyExample example);

    int updateByExample(@Param("record") County record, @Param("example") CountyExample example);

    int updateByPrimaryKeySelective(County record);

    int updateByPrimaryKey(County record);

    public static final String COLUMNS = "id, city_code, code, name, create_time, update_time";

    public static final String TABLE_NAME = "county";
}