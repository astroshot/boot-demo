package com.boot.dao.mapper.custom;

import com.boot.dao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;


public interface UserCustomMapper {

    @InsertProvider(type = Provider.class, method = "saveAll")
    int saveAll(@Param("list") List<User> users);

    class Provider {
        public String saveAll(Map map) {
            // key in map is the same with @Param annotation
            List<User> users = (List<User>) map.get("list");
            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO ");
            sb.append("`user`");
            sb.append(" (`name`, `email`, `phone`, `status`, `created_at`, `updated_at`) ");
            sb.append(" VALUES ");
            // element `list` here is the same with @Param annotation
            MessageFormat mf = new MessageFormat(
                    "(#'{'list[{0}].name'}', )" +
                            "(#'{'list[{0}].email'}', )" +
                            "(#'{'list[{0}].phone'}', )" +
                            "(#'{'list[{0}].status'}', )" +
                            "(#'{'list[{0}].createdAt'}', )" +
                            "(#'{'list[{0}].updatedAt'}', )"
            );

            for (int i = 0; i < users.size(); i++) {
                sb.append(mf.format(new Object[]{i}));
                if (i < users.size() - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
    }

    @Insert("<script>" +
            "INSERT INTO `user` " +
            "(`name`, `email`, `phone`, `status`, `created_at`, `updated_at`) " +
            "VALUES " +
            "<foreach collection='list' item='list' separator=',' > " +
            "(#{list.name}, #{list.email}, #{list.phone}, #{list.status}, #{list.createdAt}, #{list.updatedAt}) " +
            "</foreach>" +
            "</script>")
    int insertAll(@Param("list") List<User> users);
}
