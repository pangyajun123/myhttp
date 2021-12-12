package com.iwbfly.myhttp.reflection;

import com.iwbfly.myhttp.Client;
import com.iwbfly.myhttp.HttpMethod;
import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.converter.json.JsonConverter;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.exceptions.RetryableException;
import com.iwbfly.myhttp.http.*;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.interceptor.InterceptorChain;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.mapping.MappingTemplate;
import com.iwbfly.myhttp.retry.Retryer;
import com.iwbfly.myhttp.utils.DataType;
import com.iwbfly.myhttp.utils.RequestNameValue;
import com.iwbfly.myhttp.utils.StringUtils;
import okhttp3.MediaType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import  com.iwbfly.myhttp.mapping.MappingParameter.*;

import static com.iwbfly.myhttp.mapping.MappingParameter.*;
import static java.lang.String.format;

/**
 * @auther: pangyajun
 * @create: 2021/10/14 15:43
 **/
public class MyhttpRequest<T> {

    private Myhttp.Builder configuration;

    private String protocol;

    private String url;

    private Map<String, Object> query = new LinkedHashMap<>();

    private final Object[] arguments;

    private MyhttpHeaderMap headers = new MyhttpHeaderMap();

    private Map<String, Object> data = new LinkedHashMap<String, Object>();

    private List bodyList = new LinkedList<>();

    private MyhttpRequestType type;

    private String requestBody;

    private String charset;

    private String responseEncode = "UTF-8";

    private boolean async;

    private DataType dataType;

    private int timeout;

    private Logger.Level loggerLevel;

    private Logger logger;

    private JsonConverter jsonConverter;

    private Type returnClass;

    private Request.Options options;

    private InterceptorChain interceptorChain = new InterceptorChain();
    public final  String TYPE_APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public final  String TYPE_APPLICATION_JSON = "application/json";
    public final  String TYPE_MULTIPART_FORM_DATA = "multipart/form-data";
    private  Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public MyhttpRequest(Myhttp.Builder configuration, Object[] arguments) {
        this.configuration = configuration;
        this.arguments = arguments;
    }
    public MyhttpRequest(Myhttp.Builder configuration) {
        this(configuration, new Object[0]);
    }

    public JsonConverter getJsonConverter() {
        return jsonConverter;
    }

    public MyhttpRequest<T> setJsonConverter(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
        return this;
    }

    public Type getReturnClass() {
        return returnClass;
    }

    public MyhttpRequest<T> setReturnClass(Type returnClass) {
        this.returnClass = returnClass;
        return this;
    }

    public Myhttp.Builder getConfiguration() {
        return configuration;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public MyhttpHeaderMap getHeaders() {
        return headers;
    }

    public MyhttpRequestType getType() {
        return type;
    }

    public String getCharset() {
        return charset;
    }

    public String getResponseEncode() {
        return responseEncode;
    }

    public boolean isAsync() {
        return async;
    }

    public DataType getDataType() {
        return dataType;
    }

    public InterceptorChain getInterceptorChain() {
        return interceptorChain;
    }

    public MyhttpRequest<T> setConfiguration(Myhttp.Builder configuration) {
        this.configuration = configuration;
        return this;
    }

    public MyhttpRequest<T> setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public MyhttpRequest<T> setUrl(String url) {
        this.url = url;
        return this;
    }

    public MyhttpRequest<T> setQuery(Map<String, Object> query) {
        this.query = query;
        return this;
    }

    public MyhttpRequest<T> setHeaders(MyhttpHeaderMap headers) {
        this.headers = headers;
        return this;
    }

    public MyhttpRequest<T> addHeader(String name, Object value) {
        if (StringUtils.isEmpty(name)) {
            return this;
        }
        this.headers.setHeader(name, String.valueOf(value));
        return this;
    }

    public MyhttpRequest<T> addHeader(RequestNameValue nameValue) {
        this.addHeader(nameValue.getName(), nameValue.getValue());
        return this;
    }


    public MyhttpRequest<T> addHeaders(List<RequestNameValue> nameValues) {
        for (RequestNameValue nameValue : nameValues) {
            this.addHeader(nameValue.getName(), nameValue.getValue());
        }
        return this;
    }
    public MyhttpRequest<T> setType(MyhttpRequestType type) {
        this.type = type;
        return this;
    }

    public MyhttpRequest<T> setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public MyhttpRequest<T> setResponseEncode(String responseEncode) {
        this.responseEncode = responseEncode;
        return this;
    }

    public MyhttpRequest<T> setAsync(boolean async) {
        this.async = async;
        return this;
    }

    public MyhttpRequest<T> setDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public MyhttpRequest<T> setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Logger getLogger() {
        return logger;
    }

    public MyhttpRequest<T> setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public MyhttpRequest<T> setInterceptorChain(InterceptorChain interceptorChain) {
        this.interceptorChain = interceptorChain;
        return this;
    }

    public List getBodyList() {
        return bodyList;
    }

    public MyhttpRequest<T> setBodyList(List bodyList) {
        this.bodyList = bodyList;
        return this;
    }
    public String getContentType() {
        return headers.getValue("Content-Type");
    }

    public MyhttpRequest setContentType(String contentType) {
        addHeader("Content-Type", contentType);
        return this;
    }

    public String getContentEncoding() {
        return headers.getValue("Content-Encoding");
    }

    public MyhttpRequest setContentEncoding(String contentEncoding) {
        addHeader("Content-Encoding", contentEncoding);
        return this;
    }

    public String getUserAgent() {
        return headers.getValue("User-Agent");
    }

    public MyhttpRequest setUserAgent(String userAgent) {
        addHeader("User-Agent", userAgent);
        return this;
    }
    public Map<String, Object> getData() {
        return data;
    }

    public MyhttpRequest<T> setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public MyhttpRequest<T> setRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public MyhttpRequest<T> addData(List<RequestNameValue> data) {
        putMapAddList(this.data, data);
        return this;
    }

    public MyhttpRequest<T> addData(RequestNameValue nameValue) {
        if (nameValue.isInQuery()) {
            addQuery(nameValue.getName(), nameValue.getValue());
        } else if (nameValue.isInBody()) {
            this.data.put(nameValue.getName(), nameValue.getValue());
        }
        return this;
    }

    public Logger.Level getLoggerLevel() {
        return loggerLevel;
    }

    public MyhttpRequest<T> setLoggerLevel(Logger.Level loggerLevel) {
        this.loggerLevel = loggerLevel;
        return this;
    }

    public Request.Options getOptions() {
        return options;
    }

    public MyhttpRequest<T> setOptions(Request.Options options) {
        this.options = options;
        return this;
    }

    private void putMapAddList(Map<String, Object> map, List<RequestNameValue> source) {
        for (int i = 0; i < source.size(); i++) {
            RequestNameValue nameValue = source.get(i);
            if (nameValue.isInQuery()) {
                addQuery(nameValue.getName(), nameValue.getValue());
            } else if (nameValue.isInBody()) {
                map.put(nameValue.getName(), nameValue.getValue());
            }
        }
    }

    public MyhttpRequest<T> addQuery(String name, Object value) {
        this.query.put(name, value);
        return this;
    }
    public List<RequestNameValue> getQueryNameValueList() {
        List<RequestNameValue> nameValueList = new ArrayList<>();
        for (Iterator<Map.Entry<String, Object>> iterator = query.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                RequestNameValue nameValue = new RequestNameValue(name, value, TARGET_QUERY);
                nameValueList.add(nameValue);
            }
        }
        return nameValueList;

    }
    public List<RequestNameValue> getHeaderNameValueList() {
        List<RequestNameValue> nameValueList = new ArrayList<RequestNameValue>();
        for (Iterator<MyhttpHeader> iterator = headers.headerIterator(); iterator.hasNext(); ) {
            MyhttpHeader header = iterator.next();
            RequestNameValue nameValue = new RequestNameValue(header.getName(), header.getValue(), TARGET_HEADER);
            nameValueList.add(nameValue);
        }
        return nameValueList;
    }

    public List<RequestNameValue> getDataNameValueList() {
        List<RequestNameValue> nameValueList = new ArrayList<>();
        for (Iterator<Map.Entry<String, Object>> iterator = data.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> entry = iterator.next();
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                RequestNameValue nameValue = new RequestNameValue(name, value, TARGET_BODY);
                nameValueList.add(nameValue);
            }
        }
        return nameValueList;
    }

    public MyhttpRequest<T> addInterceptor(Interceptor interceptor) {
        interceptorChain.addInterceptor(interceptor);
        return this;
    }
    public String buildUrl() {

        String query = paramBuild(getQueryNameValueList());
        if (StringUtils.isNotEmpty(query)) {
            return url + "?" + query;
        }
        return url;
    }

    public  String paramBuild(List<RequestNameValue> data){
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            RequestNameValue nameValue = data.get(i);
            /*if (!nameValue.isInQuery()) {
                continue;
            }*/
            paramBuilder.append(nameValue.getName());
            String value = MappingTemplate.getParameterValue(jsonConverter, nameValue.getValue());
            paramBuilder.append('=');
            if (StringUtils.isNotEmpty(value) && getCharset() != null) {
                String encodedValue = null;
                try {
                    encodedValue = URLEncoder.encode(value, getCharset());
                } catch (UnsupportedEncodingException e) {
                }
                if (encodedValue != null) {
                    paramBuilder.append(encodedValue);
                }
            }
            if (i < data.size() - 1) {
                paramBuilder.append('&');
            }
        }
        return paramBuilder.toString();
    }
    public  Map<String, Collection<String>> buildHeaders(){
        Map<String, Collection<String>> headers = new HashMap<>();
        List<RequestNameValue> headerList = getHeaderNameValueList();
        String contentType = getContentType();
        String contentEncoding = getContentEncoding();
        List<String> valueList ;
        if (headerList != null && !headerList.isEmpty()) {
            for (RequestNameValue nameValue : headerList) {
                String name = nameValue.getName();
                if (!name.equalsIgnoreCase("Content-Type")
                        && !name.equalsIgnoreCase("Content-Encoding")) {
                    String value = MappingTemplate.getParameterValue(jsonConverter, nameValue.getValue());
                    if(headers.containsKey(name)) {
                        headers.get(name).add(value);

                    }else {
                        valueList = new ArrayList<>();
                        valueList.add(value);
                        headers.put(name,valueList);
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(contentType) && !contentType.startsWith("multipart/form-data")) {
            valueList = new ArrayList<>();
            valueList.add(contentType) ;
            headers.put("Content-Type", valueList);
        }
        if (StringUtils.isNotEmpty(contentEncoding)) {
            valueList = new ArrayList<>();
            valueList.add(contentEncoding) ;
            headers.put("Content-Type", valueList);
        }
        return headers;
    }

    public  byte[] buildBody() {
        boolean isMethodWithBody = getType().match(HttpMethod.POST.name())||
                getType().match(HttpMethod.PUT.name() ) ||getType().match(HttpMethod.PATCH.name());
        if(!isMethodWithBody)
            return null;
        String contentType = getContentType();
        if (StringUtils.isEmpty(contentType)) {
            String value = getHeaders().getValue("Content-Type");
            if (value != null) {
                if (value.length() > 0) {
                    contentType = value;
                }
//                getHeaders().remove("Content-Type");
            }
        }

        if (StringUtils.isEmpty(contentType)) {
            contentType = TYPE_APPLICATION_X_WWW_FORM_URLENCODED;
            setContentType(contentType);
        }

        String[] typeGroup = contentType.split(";[ ]*charset=");
        String mineType = typeGroup[0];
        String charset = getCharset();
        boolean mergeCharset = false;
        if (StringUtils.isEmpty(charset)) {
            if (typeGroup.length > 1) {
                charset = typeGroup[1];
                mergeCharset = true;
            } else {
                charset = "UTF-8";
            }
        }
        String requestBody = getRequestBody();
        if (StringUtils.isEmpty(mineType)) {
            mineType = TYPE_APPLICATION_X_WWW_FORM_URLENCODED;
        }

        if (requestBody != null) {
            return setStringBody(requestBody, charset, contentType, mergeCharset);

        }
        List<RequestNameValue> nameValueList = getDataNameValueList();
        if (mineType.equals(TYPE_APPLICATION_X_WWW_FORM_URLENCODED)
                && nameValueList.size() > 0) {
            return setFormBody();
        }
        else if (mineType.equals(TYPE_APPLICATION_JSON)) {

            List bodyList = getBodyList();
            Map<String, Object> map = convertNameValueListToMap( nameValueList);
            if (map != null && !map.isEmpty()) {
                bodyList.add(map);
            }
            Object toJsonObj = bodyList;
            if (bodyList.size() == 1) {
                toJsonObj = bodyList.get(0);
            }
            String text = jsonConverter.encode(toJsonObj);
            return setStringBody(text, charset, contentType, mergeCharset);
        }

        else  {
            Map<String, Object> map = convertNameValueListToMap(nameValueList);
            StringBuilder builder = new StringBuilder();
            for (Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, Object> entry = iterator.next();
                Object value = entry.getValue();
                builder.append(value);
            }
            List bodyList = getBodyList();
            for (Object bodyItem : bodyList) {
                builder.append(bodyItem.toString());
            }
            return setStringBody( builder.toString(), charset, contentType, mergeCharset);
        }
    }
    private  Map<String, Object> convertNameValueListToMap(List<RequestNameValue> nameValueList) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < nameValueList.size(); i++) {
            RequestNameValue nameValue = nameValueList.get(i);
            String name = nameValue.getName();
            Object value = nameValue.getValue();
            if (value instanceof Date) {
                value = MappingTemplate.getParameterValue(jsonConverter, value);
            }
            if (value == null && StringUtils.isNotEmpty(name)) {
                if (name.charAt(0) == '{') {
                    Map nameMap = jsonConverter.decode(name, Map.class);
                    if (nameMap != null && nameMap.size() > 0) {
                        map.putAll(nameMap);
                    } else {
                        map.put(name, value);
                    }
                }
            }
            else {
                map.put(name, value);
            }
        }
        return map;
    }
    protected  byte[] setFormBody() {
        String s = paramBuild(getDataNameValueList());
        if (StringUtils.isEmpty(s))
            return null;
        return s.getBytes();
    }
    protected  byte[] setStringBody(String text, String charset, String contentType, boolean mergeCharset) {
        MediaType mediaType = MediaType.parse(contentType);
        Charset cs = DEFAULT_CHARSET;
        if (StringUtils.isNotEmpty(charset)) {
            try {
                cs = Charset.forName(charset);
            } catch (Throwable th) {
                throw new MyhttpRuntimeException("[Myhttp] '" + charset + "' is not a valid charset", th);
            }
        }
        if (contentType != null) {
            Charset mtcs = mediaType.charset();
            if (mtcs == null) {
                if (StringUtils.isNotEmpty(charset) && mergeCharset) {
                    mediaType = MediaType.parse(contentType + "; charset=" + charset.toLowerCase());
                }
            }
        }
        return text.getBytes(cs);

    }
    long elapsedTime(long start) {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }
    public Request create(){
        interceptorChain.beforeExecute(this);
        String method = type.getName();
        Charset charset = Charset.forName(getCharset());
        String url = buildUrl();
        byte[] bytes = buildBody();
        Map<String, Collection<String>> headers = buildHeaders();
        return Request.create(method,url,headers,bytes,charset);
    }
    public Object execute(Request request){
        if (loggerLevel != Logger.Level.NONE)
        logger.logRequest(request,this);
        Client client = configuration.getClient();
        String result = null;
        Object decode = null;
        Response response = null;
        long start = System.nanoTime();
        try {
            response = client.execute(request, options);
        } catch (IOException e) {
            if (loggerLevel != Logger.Level.NONE)
                logger.logIOException( e, elapsedTime(start));
            throw new RetryableException(e, format("%s %s executing  %s",  request.httpMethod(), request.url(),e.getMessage()));
        }
        try {
            long elapsedTime = elapsedTime(start);
            if (loggerLevel != Logger.Level.NONE)
                response = logger.logAndRebufferResponse(response, elapsedTime,this);
            if (response != null) {
                if (returnClass == Response.class) {
                    return response;
                }
                result = response.body().string();
                decode = jsonConverter.decode(result, returnClass);
            }
        } catch (Throwable e) {
            return result;
        }

        return decode;
    }


}
