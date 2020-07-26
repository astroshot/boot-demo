package com.boot.common.web.resolver;

import com.boot.common.model.JSONResponse;
import com.boot.common.web.enums.UnifiedCode;
import com.boot.common.web.exception.ServiceException;
import com.boot.common.web.helper.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.nio.charset.StandardCharsets;

public class ControllerExceptionResolver extends AbstractHandlerExceptionResolver{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response, Object o, Exception exception) {

        if (!(exception instanceof BindException || exception instanceof MethodArgumentNotValidException
                || exception instanceof ValidationException || exception instanceof ServiceException)) {
            response.reset();
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.setStatus(HttpStatus.OK.value());

            try {
                String callback = request.getParameter("callback");
                String jsonString;
                String result;
                if (exception instanceof WebCommonException) {
                    WebCommonException we = (WebCommonException) exception;
                    jsonString = JSONResponse.error(we.getCode(), we.getMessage()).toJsonString();
                } else {
                    jsonString = JSONResponse.error(UnifiedCode.UNKNOWN).toJsonString();
                }

                if (callback != null && !"".equals(callback)) {
                    result = String.format("%s(%s)", callback, jsonString);
                } else {
                    result = jsonString;
                }

                ResponseHelper.response(response, MimeTypeUtils.APPLICATION_JSON_VALUE, result);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
}
