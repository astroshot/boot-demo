package com.boot.common.web.validator;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.enums.ExceptionCode;
import com.boot.common.web.enums.UnifiedCode;
import com.boot.common.web.model.ExceptionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@EnableWebMvc
public class ControllerValidatorAdvice {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(org.springframework.validation.BindException.class)
    public JSONResponse<?> handleValidatedException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ExceptionInfo> exceptions = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            logger.error(error.getField() + ":" + error.getDefaultMessage());
            ExceptionInfo info = new ExceptionInfo();
            info.setParameterName(error.getField());
            info.setInfo(error.getDefaultMessage());
            logger.error("validate fail, parameterName: {}, msg: {}", error.getField(), error.getDefaultMessage());
            exceptions.add(info);
        }
        ExceptionCode code = UnifiedCode.PARAMETER_ERROR;
        return JSONResponse.create(code.getCode(), exceptions, code.getDescription());
    }
}
