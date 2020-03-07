package com.boot.common.web.helper;

import com.boot.common.web.model.LogInfo;
import com.boot.common.web.model.LogInfoContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public abstract class ResponseHelper {

    protected static Logger logger = LoggerFactory.getLogger(ResponseHelper.class);

    public static void response(HttpServletResponse response, String contentType, String content) {
        PrintWriter pw = null;
        response.reset();
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        if (StringUtils.isNotBlank(content)) {
            response.setContentType(contentType);
        }
        response.setStatus(HttpStatus.OK.value());
        LogInfo.LogInfoBuilder builder = LogInfoContext.getLogBuilder();
        builder.responseContent(content).status(response.getStatus());
        try {
            pw = response.getWriter();
            pw.print(content);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
