package com.boot.common.helper;


import com.boot.common.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.boot.common.constant.CommonConstant.DEFAULT_CHARSET;


public abstract class HTTPHelper {

    protected static Logger logger = LoggerFactory.getLogger(HTTPHelper.class);

    protected final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * timeout: unit ms
     */
    protected final static int TIMEOUT = 25000;

    protected final static RequestConfig REQUEST_CONFIG;

    protected final static CloseableHttpClient HTTP_CLIENT;

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT));

        REQUEST_CONFIG = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .setRedirectsEnabled(true).build();

        HTTP_CLIENT = HttpClientBuilder.create().build();
    }


    protected static String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*\"?([^\\s;\"]*)");
        Matcher m = charsetPattern.matcher(contentType);

        if (!m.find()) {
            return null;
        }

        String charset = m.group(1).trim();
        if (Charset.isSupported(charset)) {
            return charset;
        }

        charset = charset.toUpperCase(Locale.ENGLISH);
        if (Charset.isSupported(charset)) {
            return charset;
        }

        return null;
    }

    protected static String getCharset(String charset, HttpResponse response) {

        if (charset != null && !"".equals(charset)) {
            return charset;
        }

        Header contentType = response.getFirstHeader("Content-Type");
        if (contentType != null) {
            charset = getCharsetFromContentType(contentType.getValue());
        }
        return charset == null ? DEFAULT_CHARSET : charset;
    }


    protected static String parseResponse(HttpResponse response) throws IOException {

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            throw new RuntimeException("HTTP status error: " + response.getStatusLine().getStatusCode());
        }

        return EntityUtils.toString(response.getEntity(), getCharset(null, response));
    }

    /**
     * HTTP GET method
     *
     * @param url     request url
     * @param header  request header
     * @param param   query param
     * @param charset request chaset
     * @return String
     * @throws Exception timeout or service not available
     */
    public static String get(String url, Map<String, String> header, Map<String, String> param, String charset)
            throws Exception {
        // query parameters
        StringJoiner joiner = new StringJoiner("&");

        if (param != null && !param.isEmpty()) {
            param.forEach((k, v) -> {
                if (v == null) {
                    return;
                }
                try {
                    joiner.add(String.format("%s=%s", k, URLEncoder.encode(v, "utf-8")));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            });
        }

        HttpGet httpGet = new HttpGet(url + "?" + joiner.toString());
        httpGet.setConfig(REQUEST_CONFIG);

        if (header != null && !header.isEmpty()) {
            header.forEach(httpGet::setHeader);
        }

        httpGet.setHeader("Accept-Encoding", "gzip, deflate");

        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet)) {
            return parseResponse(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * HTTP POST method with form body
     *
     * @param url     request url
     * @param header  request header
     * @param param   form parameters
     * @param charset request charset
     * @return String
     * @throws Exception timeout or server unavailable
     */
    public static String postForm(String url, Map<String, String> header, Map<String, String> param, String charset)
            throws Exception {
        // form parameters
        StringJoiner joiner = new StringJoiner("&");

        if (param != null && !param.isEmpty()) {
            param.forEach((k, v) -> {
                if (v != null) {
                    joiner.add(k + "=" + v);
                }
            });
        }

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);

        if (header != null && !header.isEmpty()) {
            header.forEach(httpPost::setHeader);
        }

        // set ContentType
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        StringEntity entity = new StringEntity(joiner.toString(), DEFAULT_CHARSET.toLowerCase());
        entity.setContentEncoding(DEFAULT_CHARSET);
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            return parseResponse(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * HTTP POST method with JSON body
     *
     * @param url      request url
     * @param header   request header
     * @param jsonBody request body
     * @param charset  request charset
     * @return String
     * @throws Exception
     */
    public static String postJson(String url, Map<String, String> header, Object jsonBody, String charset)
            throws Exception {

        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(REQUEST_CONFIG);

        if (header != null && !header.isEmpty()) {
            header.forEach(httpPost::setHeader);
        }

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        httpPost.setHeader("Accept-Encoding", "gzip, deflate");
        StringEntity entity = new StringEntity(
                OBJECT_MAPPER.writeValueAsString(jsonBody), DEFAULT_CHARSET.toLowerCase());
        entity.setContentEncoding(DEFAULT_CHARSET);
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost)) {
            return parseResponse(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

}
