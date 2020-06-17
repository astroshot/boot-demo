package com.boot.common.web.validator;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.enums.ExceptionCode;
import com.boot.common.web.enums.UnifiedCode;
import com.boot.common.web.model.ExceptionInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
@EnableWebMvc
public class ControllerValidatorAdvice {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
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

    @ExceptionHandler({ValidationException.class})
    public JSONResponse<?> handleValidatedException(RuntimeException e) {
        String message = e.getMessage();
        List<ExceptionInfo> exceptions = new ArrayList<>();

        if (StringUtils.isNotBlank(message)) {
            String[] texts = message.split(", ");
            Pattern pattern = Pattern.compile("^([^\\.]+?)\\.([^:]+?): (.+?)$");
            for (String text : texts) {
                Matcher m = pattern.matcher(text);
                if (m.find()) {
                    ExceptionInfo info = new ExceptionInfo();
                    info.setParameterName(m.group(2));
                    info.setInfo(m.group(3));
                    exceptions.add(info);
                }
            }
        }
        ExceptionCode code = UnifiedCode.PARAMETER_ERROR;
        return JSONResponse.create(code.getCode(), exceptions, code.getDescription());
    }
}
