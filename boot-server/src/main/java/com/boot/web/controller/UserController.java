package com.boot.web.controller;

import com.boot.common.model.JSONResult;
import com.boot.common.model.Pager;
import com.boot.common.web.controller.BaseController;
import com.boot.dao.model.User;
import com.boot.dao.service.UserService;
import com.boot.dao.service.model.UserQueryBO;
import com.boot.web.model.UserQueryVO;
import com.boot.web.model.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping(value = "/users")
    public JSONResult<?> users(@RequestBody(required = false) UserVO vo) {
        User user = new User();
        user.setEmail(vo.getEmail());
        user.setName(vo.getName());
        user.setPhone(vo.getPhone());
        user.setStatus(0);
        userService.saveOrUpdate(user);
        return JSONResult.success(true);
    }

    @GetMapping(value = "/users")
    public JSONResult<?> getUsers(@Valid UserQueryVO param) {

        if (param.getPageNo() < 1 || param.getPageSize() < 1) {
            return JSONResult.error("pageNo and pageSize should be positive!");
        }

        UserQueryBO condition = new UserQueryBO();
        condition.setName(param.getName());
        condition.setEmail(param.getEmail());
        condition.setPageNo(param.getPageNo());
        condition.setPageSize(param.getPageSize());

        Pager<User> users = userService.getBy(condition);
        return JSONResult.success(users);
    }
}