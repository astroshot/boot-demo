package com.boot.dao.mapper;

import com.boot.dao.model.User;
import com.boot.dao.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    public static final String COLUMNS = "id, name, email, phone, type, description, status, created_at, updated_at";

    public static final String TABLE_NAME = "user";

    int batchInsert(@Param("list") List<User> list);

    int batchInsertSelective(@Param("list") List<User> list, @Param("columns") String ... columns);

    int insertOrUpdateOnDuplicateKey(@Param("record") User record, @Param("columns") String ... columns);

    int insertOrUpdateSelectiveOnDuplicateKey(@Param("record") User record, @Param("columns") String ... columns);

    int batchInsertOrUpdateSelectedColumnsOnDuplicateKey(@Param("list") List<User> list, @Param("insertionColumns") List<String> insertionColumns, @Param("updatingColumns") List<String> updatingColumns);
}