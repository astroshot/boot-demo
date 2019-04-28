package com.boot.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.dao.model.User;
import com.boot.dao.service.UserService;
import com.boot.web.model.UserVO;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "/users", method = {RequestMethod.POST})
    public boolean users(UserVO vo) {
        User user = new User();
        user.setEmail(vo.getEmail());
        user.setName(vo.getName());
        user.setPhone(vo.getPhone());
        user.setStatus(0);
        userService.saveOrUpdate(user);
        return true;
    }

}
