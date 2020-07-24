package com.boot.web.controller;

import com.boot.common.dao.enums.UserStatus;
import com.boot.common.model.JSONResponse;
import com.boot.common.model.Pager;
import com.boot.common.web.controller.BaseController;
import com.boot.dao.model.User;
import com.boot.dao.service.UserService;
import com.boot.dao.service.model.UserBO;
import com.boot.dao.service.model.UserQueryBO;
import com.boot.web.model.UserQueryVO;
import com.boot.web.model.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api("User Controller")
@RestController
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "创建用户", notes = "创建用户", tags = "用户")
    @ApiImplicitParam(paramType = "body", dataType = "UserVO", required = true)
    @PostMapping(value = "/users")
    public JSONResponse<?> users(@Valid @RequestBody UserVO vo) {
        User user = new User();
        user.setEmail(vo.getEmail());
        user.setName(vo.getName());
        user.setPhone(vo.getPhone());
        user.setStatus(UserStatus.NORMAL.getValue());
        userService.saveOrUpdate(user);
        return JSONResponse.success(true);
    }


    @ApiOperation(value = "查询用户", notes = "查询用户", tags = "用户")
    @ApiImplicitParam(paramType = "query", dataType = "UserQueryVO", required = true)
    @GetMapping(value = "/users")
    public JSONResponse<?> getUsers(@Valid UserQueryVO param) {

        if (param.getPageNo() < 1 || param.getPageSize() < 1) {
            return JSONResponse.error("pageNo and pageSize should be positive!");
        }

        UserQueryBO condition = new UserQueryBO();
        condition.setName(param.getName());
        condition.setEmail(param.getEmail());
        condition.setPageNo(param.getPageNo());
        condition.setPageSize(param.getPageSize());

        Pager<User> users = userService.getBy(condition);
        return JSONResponse.success(users);
    }

    @PostMapping("/all-users")
    public JSONResponse<?> saveAllUsers(@RequestBody(required = false) List<UserBO> users) {
        if (CollectionUtils.isEmpty(users)) {
            return JSONResponse.success("users 为空");
        }

        int res = userService.insertAll(users);
        return JSONResponse.create(0, res > 0, "");
    }

    @PostMapping("/update/phone")
    JSONResponse<?> updatePhone(@RequestBody(required = false) @Valid User user) {
        int res = userService.insertOrUpdateOnDuplicatePhone(user);
        return JSONResponse.success(res > 0);
    }
}
