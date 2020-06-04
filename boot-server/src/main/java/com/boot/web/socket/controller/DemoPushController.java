package com.boot.web.socket.controller;

import com.boot.common.model.JSONResult;
import com.boot.common.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoPushController extends BaseController {

    @Autowired
    protected DemoSocketController demoSocketController;

    @RequestMapping("/push")
    public JSONResult<?> push(String appNo, String msg) {
        demoSocketController.OnMessage(appNo, msg);
        return JSONResult.success();
    }

}
