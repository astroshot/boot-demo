package com.boot.dao.service;

import com.boot.dao.model.User;
import com.boot.dao.service.model.UserBO;
import com.boot.dao.service.model.UserQueryBO;

import java.util.List;

public interface UserService {

    void saveOrUpdate(User user);

    List<User> getBy(UserQueryBO condition);

    boolean saveAll(List<UserBO> users);

}
