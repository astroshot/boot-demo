package com.boot.common.web.filter;

import com.boot.common.web.filter.wrapper.RequestBodyCacheWrapper;
import com.boot.common.web.helper.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * set order to the first filter.
 */
public class RequestBodyCacheWrapFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (RequestHelper.isFormBody(request)) {
                filterChain.doFilter(request, response);
            } else {
                filterChain.doFilter(new RequestBodyCacheWrapper(request), response);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

}
