package com.boot.dao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.boot.dao.mapper.UserMapper;
import com.boot.dao.model.User;
import com.boot.dao.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void saveOrUpdate(User user) {
        if (user.getId() == null) {
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            userMapper.insert(user);
        } else {
            user.setUpdatedAt(new Date());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

}
