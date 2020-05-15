package com.boot.web.controller;

import com.boot.common.web.controller.BaseController;
import com.boot.timing.UserScheduler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("timing")
public class TimingController extends BaseController {

    @Resource
    protected UserScheduler userScheduler;

    @GetMapping("/user")
    public String processUser() {
        UserScheduler.setAvailableTime(System.currentTimeMillis() + 5 * 60 * 1000);
        return "ok";
    }
}
