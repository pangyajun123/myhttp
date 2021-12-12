package com.iwbfly.myhttp.reflection;

import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.retry.Retryer;

import java.lang.annotation.Annotation;

/**
 * @auther: pangyajun
 * @create: 2021/10/15 13:21
 **/
public class MetaRequest {

    private Annotation requestAnnotation;

    /**
     * target http url
     */
    private String url;

    /**
     * http method type: <br>
     *     GET POST PUT HEAD OPTIONS DELETE PATCH TRACE
     */
    private String type;

    /**
     * type of response data: <br>
     *     text json xml <br>
     * default value is "auto"
     */
    private String dataType;

    /**
     * whether can use async http request or not
     */
    private boolean async;

    private Integer timeout;

    /**
     * SSL protocol
     */
    private String sslProtocol;

    /**
     * Class of retryer
     */
    private Class<Retryer> retryer;

    /**
     * max count to retry
     */
    private int retryCount;

    private long maxRetryInterval;

    /**
     * Content Type
     */
    private String contentType;

    /**
     * Content Encoding
     */
    private String contentEncoding;

    /**
     * Charset, Default is UTF-8
     */
    private String charset;

    /**
     * User Agent
     */
    private String userAgent;

    /**
     * Request Headers
     */
    private String[] headers;

    /**
     * 拦截器类数组
     */
    private Class<?>[] interceptor;

    private String[] data;

    private long progressStep;

    private Class<?> decoder;

    /**
     * KeyStore Id
     */
    private String keyStore;

    /**
     * 是否允许打印请求/响应日志
     */
    private Logger.Level loggerLevel;

    public MetaRequest(Annotation requestAnnotation) {
        this.requestAnnotation = requestAnnotation;
    }

    public MetaRequest() {
    }

    public Annotation getRequestAnnotation() {
        return requestAnnotation;
    }

    public void setRequestAnnotation(Annotation requestAnnotation) {
        this.requestAnnotation = requestAnnotation;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }

    public Class<Retryer> getRetryer() {
        return retryer;
    }

    public void setRetryer(Class retryer) {
        this.retryer = retryer;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getMaxRetryInterval() {
        return maxRetryInterval;
    }

    public void setMaxRetryInterval(long maxRetryInterval) {
        this.maxRetryInterval = maxRetryInterval;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public Class<?>[] getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(Class<?>[] interceptor) {
        this.interceptor = interceptor;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public long getProgressStep() {
        return progressStep;
    }

    public void setProgressStep(long progressStep) {
        this.progressStep = progressStep;
    }

    public Class<?> getDecoder() {
        return decoder;
    }

    public void setDecoder(Class<?> decoder) {
        this.decoder = decoder;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public Logger.Level getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(Logger.Level loggerLevel) {
        this.loggerLevel = loggerLevel;
    }
}

