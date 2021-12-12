package com.iwbfly.myhttp;

import com.iwbfly.myhttp.converter.Decoder;
import com.iwbfly.myhttp.converter.json.FastJsonConverter;

import com.iwbfly.myhttp.converter.json.JsonConverter;
import com.iwbfly.myhttp.converter.text.DefaultTextConverter;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.filter.Filter;
import com.iwbfly.myhttp.http.Request;
import com.iwbfly.myhttp.interceptor.InterceptorFactory;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.logging.Slf4jLogger;
import com.iwbfly.myhttp.proxy.ProxyFactory;
import com.iwbfly.myhttp.retry.Retryer;
import com.iwbfly.myhttp.utils.DataType;
import com.iwbfly.myhttp.utils.RequestNameValue;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther: pangyajun
 * @create: 2021/11/24 09:42
 **/
public class Myhttp {

    private Myhttp myhttp;
    public Builder builder;

    public Myhttp(Builder builder) {
        this.builder = builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Client client = new Client.Default(null, null);
        private int timeout;
        private int retryCount;
        private long maxRetryInterval;
        private Map<String, Object> variables;
        private String charset;
        private List<RequestNameValue> defaultParameters = new ArrayList<>();
        private List<RequestNameValue> defaultHeaders = new ArrayList<>();
        private InterceptorFactory interceptorFactory = new InterceptorFactory.Default();
        private List<Class> interceptors = new ArrayList<>();
        private Map<DataType, Decoder> converterMap;
        private JsonConverter jsonConverter = new FastJsonConverter();
        private boolean cacheEnabled = true;
        private Map<Class, Object> instanceCache = new ConcurrentHashMap<>();
        private Retryer retryer ;
        private Map<String, Class> filterRegisterMap = new HashMap<>();
        private Logger.Level loggerLevel;
        private Logger logger = new Slf4jLogger();
        public Myhttp.Builder build(){
            return Myhttp.builder()
                    .charset("UTF-8")
                    .retryCount(3)
                    .maxRetryInterval(800)
                    .retryer(Retryer.Never.class)
                    .cacheEnabled(true)
                    .variables(new HashMap<>())
                    .setLoggerLevel(Logger.Level.FULL)
                    .timeout(3000)
                    .defaultHeaders(new ArrayList<RequestNameValue>())
                    .defaultParameters(new ArrayList<RequestNameValue>())
                    .interceptors( new ArrayList<Class>());
        }

        public Client getClient() {
            return client;
        }

        public Builder client(Client client) {
            this.client = client;
            return this;
        }

        public <T> T createInstance(Class<T> clazz) {
            ProxyFactory<T> proxyFactory = getProxyFactory(clazz);
            return proxyFactory.createInstance();
        }
        public <T> ProxyFactory<T> getProxyFactory(Class<T> clazz) {
            return new ProxyFactory<T>(this, clazz);
        }

        public String getCharset() {
            return charset;
        }

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Map<String, Object> getVariables() {
            return variables;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        public Builder setVariableValue(String name, Object value) {
            getVariables().put(name, value);
            return this;
        }

        public Object getVariableValue(String name) {
            return getVariables().get(name);
        }

        public int getTimeout() {
            return timeout;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public int getRetryCount() {
            return retryCount;
        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public long getMaxRetryInterval() {
            return maxRetryInterval;
        }

        public Builder maxRetryInterval(long maxRetryInterval) {
            this.maxRetryInterval = maxRetryInterval;
            return this;
        }

        public List<RequestNameValue> getDefaultParameters() {
            return defaultParameters;
        }

        public Builder defaultParameters(List<RequestNameValue> defaultParameters) {
            this.defaultParameters = defaultParameters;
            return this;
        }

        public List<RequestNameValue> getDefaultHeaders() {
            return defaultHeaders;
        }

        public Builder defaultHeaders(List<RequestNameValue> defaultHeaders) {
            this.defaultHeaders = defaultHeaders;
            return this;
        }

        public InterceptorFactory getInterceptorFactory() {
            return interceptorFactory;
        }

        public Builder interceptorFactory(InterceptorFactory interceptorFactory) {
            this.interceptorFactory = interceptorFactory;
            return this;
        }

        public Map<DataType, Decoder> getConverterMap() {
            return converterMap;
        }

        public Builder converterMap(Map<DataType, Decoder> converterMap) {
            this.converterMap = converterMap;
            return this;
        }
        private Myhttp.Builder setTextConverter() {
            getConverterMap().put(DataType.TEXT, new DefaultTextConverter());
            return this;
        }
        public List<Class> getInterceptors() {
            return interceptors;
        }

        public Builder interceptors(List<Class> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public JsonConverter getJsonConverter() {
            return jsonConverter;
        }

        public Builder jsonConverter(JsonConverter jsonConver) {
            this.jsonConverter = jsonConverter;
            return this;
        }

        public Retryer getRetryer() {
            return retryer;
        }

        public Builder retryer(Class<?> retryer){
            try {
               this.retryer = (Retryer) retryer.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Logger getLogger() {
            return logger;
        }

        public Builder logger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Logger.Level getLoggerLevel() {
            return loggerLevel;
        }

        public Builder setLoggerLevel(Logger.Level loggerLevel) {
            this.loggerLevel = loggerLevel;
            return this;
        }

        public boolean isCacheEnabled() {
            return cacheEnabled;
        }

        public Builder cacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        public Map<Class, Object> getInstanceCache() {
            return instanceCache;
        }

        public Builder instanceCache(Map<Class, Object> instanceCache) {
            this.instanceCache = instanceCache;
            return this;
        }

        public Myhttp.Builder registerFilter(String name, Class filterClass) {
            if (!(Filter.class.isAssignableFrom(filterClass))) {
                throw new MyhttpRuntimeException("Cannot register class \"" + filterClass.getName()
                        + "\" as a filter, filter class must implement Filter interface!");
            }
            if (filterRegisterMap.containsKey(name)) {
                throw new MyhttpRuntimeException("filter \"" + name + "\" already exists!");
            }
            filterRegisterMap.put(name, filterClass);
            return this;
        }

        public Filter newFilterInstance(String name) {
            Class filterClass = filterRegisterMap.get(name);
            if (filterClass == null) {
                throw new MyhttpRuntimeException("filter \"" + name + "\" does not exists!");
            }
            try {
                return (Filter) filterClass.newInstance();
            } catch (InstantiationException e) {
                throw new MyhttpRuntimeException("An error occurred the initialization of filter \"" + name + "\" ! cause: " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new MyhttpRuntimeException("An error occurred the initialization of filter \"" + name + "\" ! cause: " + e.getMessage(), e);
            }
        }
    }


}
