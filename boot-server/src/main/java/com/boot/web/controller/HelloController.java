package com.boot.web.controller;

import com.boot.common.model.JSONResult;
import com.boot.common.web.controller.BaseController;
import com.boot.web.model.JacksonTestVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController extends BaseController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/check-health")
    public String checkHealth() {
        return "ok";
    }

    @GetMapping("/test")
    public JSONResult<?> test() {
        JacksonTestVO vo = new JacksonTestVO();
        vo.setCount(1);
        vo.setName("name");
        return JSONResult.success(vo);
    }
}
