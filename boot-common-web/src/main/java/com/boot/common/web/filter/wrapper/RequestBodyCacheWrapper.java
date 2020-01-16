package com.boot.common.web.filter.wrapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * fix request body can only be read once
 */
public class RequestBodyCacheWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RequestBodyCacheWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new BodyCacheInputStream(new ByteArrayInputStream(body));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    static class BodyCacheInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        private boolean finished = false;

        public BodyCacheInputStream(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return finished;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("Not Implemented yet.");
        }

        @Override
        public int read() throws IOException {
            int data = inputStream.read();
            if (data == -1) {
                finished = true;
            }
            return data;
        }
    }
}
