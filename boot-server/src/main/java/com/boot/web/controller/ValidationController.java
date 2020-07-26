package com.boot.web.controller;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.controller.BaseController;
import com.boot.web.model.ValidationTestListVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ValidationController extends BaseController {

    /**
     * Two ways to validate list params
     */
    @PostMapping("/test/validation-v2")
    public JSONResponse<?> testParamValidationV2(@Valid @RequestBody ValidationTestListVO param) {
        return JSONResponse.success(param);
    }

}
