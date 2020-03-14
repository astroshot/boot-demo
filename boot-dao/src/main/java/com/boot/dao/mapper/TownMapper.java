package com.boot.dao.mapper;

import com.boot.dao.model.Town;
import com.boot.dao.model.TownExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TownMapper {
    int countByExample(TownExample example);

    int deleteByExample(TownExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Town record);

    int insertSelective(Town record);

    List<Town> selectByExample(TownExample example);

    Town selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Town record, @Param("example") TownExample example);

    int updateByExample(@Param("record") Town record, @Param("example") TownExample example);

    int updateByPrimaryKeySelective(Town record);

    int updateByPrimaryKey(Town record);

    public static final String COLUMNS = "id, county_code, code, name, create_time, update_time";

    public static final String TABLE_NAME = "town";
}