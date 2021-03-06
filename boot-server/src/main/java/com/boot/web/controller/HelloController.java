package com.boot.web.controller;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.controller.BaseController;
import com.boot.web.model.JacksonTestVO;
import com.boot.web.model.ValidationTestVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
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
    public JSONResponse<?> test() {
        JacksonTestVO vo = new JacksonTestVO();
        vo.setCount(1);
        vo.setName("name");
        return JSONResponse.success(vo);
    }

    @PostMapping("/test/validation")
    public JSONResponse<?> testParameterValidation(@Valid @RequestBody ValidationTestVO[] params) {
        return JSONResponse.success(params);
    }
}
