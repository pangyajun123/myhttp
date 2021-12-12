package com.iwbfly.myhttp.springboot.support;

import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.retry.Retryer;
import com.iwbfly.myhttp.utils.RequestNameValue;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/10/13 10:46
 **/
@ConfigurationProperties("myhttp.cleint")
public class MyhttpClientProperties {

    private String beanId;
    private int timeout = -1;
    private int retryCount = 3;
    private long maxRetryInterval = 600;
    private String charset = "UTF-8";
    private boolean cacheEnabled = true;
    private List<RequestNameValue> defaultParameters;
    private List<RequestNameValue> defaultHeaders;
    private List<Class> interceptors;
    private Map<String, Object> variables = new HashMap<>();
    private Logger.Level loggerLevel = Logger.Level.FULL;
    private Class retryer = Retryer.Never.class;

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public List<RequestNameValue> getDefaultParameters() {
        return defaultParameters;
    }

    public void setDefaultParameters(List<RequestNameValue> defaultParameters) {
        this.defaultParameters = defaultParameters;
    }

    public List<RequestNameValue> getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setDefaultHeaders(List<RequestNameValue> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }

    public List<Class> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Class> interceptors) {
        this.interceptors = interceptors;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public Logger.Level getLoggerLevel() {
        return loggerLevel;
    }

    public void setLoggerLevel(Logger.Level loggerLevel) {
        this.loggerLevel = loggerLevel;
    }

    public Class getRetryer() {
        return retryer;
    }

    public MyhttpClientProperties setRetryer(Class retryer) {
        this.retryer = retryer;
        return this;
    }
}
