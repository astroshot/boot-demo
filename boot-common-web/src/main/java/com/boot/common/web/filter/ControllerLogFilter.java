package com.boot.common.web.filter;

import com.boot.common.web.model.LogInfo;
import com.boot.common.web.model.LogInfoContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


@Aspect
@Component
public class ControllerLogFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Before("within(com.boot..web.controller*..*)")
    public void controllerHandleRequest(JoinPoint jp) {
        LogInfo.LogInfoBuilder builder = LogInfoContext.getLogBuilder();
        Object[] args = jp.getArgs();

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof MultipartFile) {
                    args[i] = null;
                    logger.info("File Uploading not record...");
                } else if (args[i] instanceof ServletRequest) {
                    args[i] = null;
                    logger.info("Servlet Request not record...");
                } else if (args[i] instanceof ServletResponse) {
                    args[i] = null;
                    logger.info("Servlet Response not record...");
                }
            }
        }

        builder.methodParams(args);
        builder.serviceName(jp.getTarget().getClass().getName());
        builder.methodName(jp.getSignature().getName());
        LogInfoContext.setLogBuilder(builder);
    }

    @AfterThrowing(pointcut = "within(com.boot..web.controller*..*)", throwing = "ex")
    public void controllerHandleException(JoinPoint jp, RuntimeException ex) {
        LogInfo.LogInfoBuilder builder = LogInfoContext.getLogBuilder();
        builder.logType("ERROR");

        if (ex == null) {
            return;
        }

        builder.exceptionDetail(ex.getMessage());
        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            builder.exceptionInfo(sw.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
