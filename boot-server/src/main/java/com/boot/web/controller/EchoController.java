package com.boot.web.controller;

import com.boot.common.model.JSONResult;
import com.boot.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class EchoController extends BaseController {

    @GetMapping("/echo")
    public JSONResult<?> echo(@Valid String message) {
        return JSONResult.success(message);
    }
}
