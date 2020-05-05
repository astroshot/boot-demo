package com.boot.common.helper;

import com.boot.common.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AsyncHTTPHelper {

    protected final static Set<Integer> ACCEPTABLE_HTTP_STATUS_CODE = new HashSet<>(
            Arrays.asList(HttpStatus.SC_OK, HttpStatus.SC_CREATED, HttpStatus.SC_ACCEPTED,
                    HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, HttpStatus.SC_CREATED));
    /**
     * Jackson
     */
    protected final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int TIMEOUT = 60 * 1000;
    private static final int POOL_SIZE = 20;
    private static final int BUF_SIZE = 8192;
    protected static Logger logger = LoggerFactory.getLogger(AsyncHTTPHelper.class);

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT));
    }

    public static CloseableHttpAsyncClient createDefaultHttpAsyncClient() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setContentCompressionEnabled(true)
                // connection time out
                .setConnectTimeout(TIMEOUT)
                // http request time out
                .setConnectionRequestTimeout(TIMEOUT)
                // socket time out
                .setSocketTimeout(TIMEOUT)
                // permission to enable redirect
                .setRedirectsEnabled(true).build();

        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .setConnectTimeout(TIMEOUT)
                .setRcvBufSize(BUF_SIZE)
                .setSndBufSize(BUF_SIZE)
                .build();

        // connection pool size
        ConnectingIOReactor ioReactor;

        // connection pool
        PoolingNHttpClientConnectionManager connManager;

        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            connManager.setMaxTotal(POOL_SIZE);
            connManager.setDefaultMaxPerRoute(POOL_SIZE);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig).build();
        httpAsyncClient.start();
        return httpAsyncClient;
    }

    /**
     * Async GET Request
     */
    public static void get(CloseableHttpAsyncClient client, String url, Map<String, String> header,
                           Map<String, String> param, final FutureCallback<HttpResponse> callback, String charset) {
        StringJoiner joiner = new StringJoiner("&");
        if (param != null && !param.isEmpty()) {
            param.forEach((key, value) -> {
                if (value == null) {
                    return;
                }

                try {
                    joiner.add(URLEncoder.encode(key, charset) + "=" + URLEncoder.encode(value, charset));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
        }

        HttpGet httpGet = new HttpGet(url + "?" + joiner.toString());

        if (header != null && !header.isEmpty()) {
            header.forEach(httpGet::setHeader);
        }

        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        client.execute(httpGet, callback);
    }

    /**
     * Async POST Request with form body
     */
    public static void postForm(CloseableHttpAsyncClient client, String url, Map<String, String> headers,
                                Map<String, String> pars, final FutureCallback<HttpResponse> callback, String charset) {
        StringJoiner joiner = new StringJoiner("&");

        if (pars != null && !pars.isEmpty()) {
            pars.forEach((k, v) -> {
                if (v != null) {
                    try {
                        joiner.add(URLEncoder.encode(k, charset) + "=" + URLEncoder.encode(v, charset));
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        HttpPost httpPost = new HttpPost(url);

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::setHeader);
        }

        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        StringEntity entity = new StringEntity(joiner.toString(), StandardCharsets.UTF_8);
        entity.setContentEncoding("UTF-8");
        httpPost.setEntity(entity);
        client.execute(httpPost, callback);
    }

    public static void postJson(CloseableHttpAsyncClient client, String url, Map<String, String> headers, Object json,
                                final FutureCallback<HttpResponse> callback) throws Exception {

        HttpPost httpPost = new HttpPost(url);

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(httpPost::setHeader);
        }

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        StringEntity entity = new StringEntity(OBJECT_MAPPER.writeValueAsString(json), StandardCharsets.UTF_8);
        entity.setContentEncoding(StandardCharsets.UTF_8.name());
        httpPost.setEntity(entity);

        client.execute(httpPost, callback);
    }

    protected static String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }

        Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
        Matcher m = charsetPattern.matcher(contentType);
        if (m.find()) {
            String charset = m.group(1).trim();
            if (Charset.isSupported(charset)) {
                return charset;
            }
            charset = charset.toUpperCase(Locale.ENGLISH);
            if (Charset.isSupported(charset)) {
                return charset;
            }
        }
        return null;
    }

    private static class DefaultInstanceHolder {
        private static final CloseableHttpAsyncClient HTTP_CLIENT = createDefaultHttpAsyncClient();
    }

}
