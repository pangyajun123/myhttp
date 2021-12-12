package com.iwbfly.myhttp.reflection;


import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.annotation.BaseLifeCycle;
import com.iwbfly.myhttp.annotation.MethodLifeCycle;
import com.iwbfly.myhttp.annotation.ParamLifeCycle;
import com.iwbfly.myhttp.config.VariableScope;
import com.iwbfly.myhttp.converter.json.JsonConverter;
import com.iwbfly.myhttp.exceptions.MyhttpInterceptorDefineException;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.exceptions.RetryableException;
import com.iwbfly.myhttp.filter.Filter;
import com.iwbfly.myhttp.http.MyhttpRequestType;
import com.iwbfly.myhttp.http.Request;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.interceptor.InterceptorFactory;
import com.iwbfly.myhttp.lifecycles.BaseAnnotationLifeCycle;
import com.iwbfly.myhttp.lifecycles.LoggerAnnotationLifeCycle;
import com.iwbfly.myhttp.lifecycles.MethodAnnotationLifeCycle;
import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.mapping.MappingTemplate;
import com.iwbfly.myhttp.mapping.MappingVariable;
import com.iwbfly.myhttp.proxy.InterfaceProxyHandler;
import com.iwbfly.myhttp.retry.Retryer;
import com.iwbfly.myhttp.utils.*;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.*;

import static com.iwbfly.myhttp.mapping.MappingParameter.*;

/**
 * @auther: pangyajun
 * @create: 2021/10/14 14:09
 **/
public class MyhttpMethod<T> implements VariableScope, MyhttpMethodHandler {

    private final InterfaceProxyHandler interfaceProxyHandler;
    private final Myhttp.Builder configuration;
    private InterceptorFactory interceptorFactory;
    private final Method method;
    private String[] methodNameItems;
    private Class returnClass;
    private MetaRequest metaRequest;
    private MetaRequest baseMetaRequest;
    private MappingTemplate baseUrlTemplate;
    private MappingTemplate charsetTemplate;
    private MappingTemplate baseCharsetTemplate;
    private MappingTemplate urlTemplate;
    private MappingTemplate typeTemplate;
    private MappingTemplate dataTypeTemplate;
    private MappingTemplate contentTypeTemplate;
    private MappingTemplate userAgentTemplate;
    private MappingTemplate encodeTemplate;
    private MappingTemplate baseContentTypeTemplate;
    private MappingParameter[] parameterTemplateArray;
    private MappingTemplate[] dataTemplateArray;
    private MappingTemplate[] headerTemplateArray;
    private List<MappingParameter> namedParameters = new ArrayList<>();
    private Map<String, MappingVariable> variables = new HashMap<>();
    private List<Interceptor> globalInterceptorList;
    private List<Interceptor> baseInterceptorList;
    private List<Interceptor> interceptorList;
    private int timeout;
    private int retryCount;
    private long maxRetryInterval;
    private Retryer retryer;
    private Logger.Level loggerLevel;
    private Logger logger;
    private JsonConverter jsonConverter;

    public MappingTemplate getTypeTemplate() {
        return typeTemplate;
    }

    public void setTypeTemplate(MappingTemplate typeTemplate) {
        this.typeTemplate = typeTemplate;

    }

    public MetaRequest getMetaRequest() {
        return metaRequest;
    }

    public MyhttpMethod<T> setMetaRequest(MetaRequest metaRequest) {
        this.metaRequest = metaRequest;
        return this;
    }

    public void addVariable(String name, MappingVariable variable) {
        variables.put(name, variable);
    }

    public void addNamedParameter(MappingParameter parameter) {
        namedParameters.add(parameter);
    }

    public MyhttpMethod(InterfaceProxyHandler interfaceProxyHandler, Myhttp.Builder configuration, Method method) {
        this.interfaceProxyHandler = interfaceProxyHandler;
        this.configuration = configuration;
        this.method = method;
        this.interceptorFactory = configuration.getInterceptorFactory();
        this.methodNameItems = NameUtils.splitCamelName(method.getName());
        logger = configuration.getLogger();
        jsonConverter = configuration.getJsonConverter();
        this.baseMetaRequest = interfaceProxyHandler.getBaseMetaRequest();
        processBaseProperties();
        processInterfaceMethods();
    }

    private void processBaseProperties() {
        this.baseMetaRequest = baseMetaRequest;
        String baseUrl = baseMetaRequest.getUrl();
        if (StringUtils.isNotBlank(baseUrl)) {
            baseUrlTemplate = makeTemplate(baseUrl);
        }

        String charset = baseMetaRequest.getCharset();
        if (StringUtils.isNotBlank(charset)) {
            charsetTemplate = makeTemplate(charset);
        } else {
            charsetTemplate = makeTemplate(configuration.getCharset());
        }

        String contentEncoding = baseMetaRequest.getContentEncoding();
        if (StringUtils.isNotBlank(contentEncoding)) {
            encodeTemplate = makeTemplate(contentEncoding);
        }
        String userAgent = baseMetaRequest.getUserAgent();
        if (StringUtils.isNotBlank(userAgent)) {
            userAgentTemplate = makeTemplate(userAgent);
        }
        String baseContentType = baseMetaRequest.getContentType();
        if (StringUtils.isNotBlank(baseContentType)) {
            baseContentTypeTemplate = makeTemplate(baseContentType);
        }

        List<Class> globalInterceptorClasses = configuration.getInterceptors();
        if (globalInterceptorClasses != null && globalInterceptorClasses.size() > 0) {
            globalInterceptorList = new LinkedList<>();
            for (Class clazz : globalInterceptorClasses) {
                if (!Interceptor.class.isAssignableFrom(clazz) || clazz.isInterface()) {
                    throw new MyhttpRuntimeException("Class [" + clazz.getName() + "] is not a implement of [" +
                            Interceptor.class.getName() + "] interface.");
                }
                Interceptor interceptor = interceptorFactory.getInterceptor(clazz);
                globalInterceptorList.add(interceptor);
            }
        }

        Class[] baseInterceptorClasses = baseMetaRequest.getInterceptor();
        if (baseInterceptorClasses != null && baseInterceptorClasses.length > 0) {
            baseInterceptorList = new LinkedList<>();
            for (int cidx = 0, len = baseInterceptorClasses.length; cidx < len; cidx++) {
                Class clazz = baseInterceptorClasses[cidx];
                if (!Interceptor.class.isAssignableFrom(clazz) || clazz.isInterface()) {
                    throw new MyhttpRuntimeException("Class [" + clazz.getName() + "] is not a implement of [" +
                            Interceptor.class.getName() + "] interface.");
                }
                Interceptor interceptor = interceptorFactory.getInterceptor(clazz);
                baseInterceptorList.add(interceptor);
            }
        }
        List<Annotation> baseAnnotationList = interfaceProxyHandler.getBaseAnnotations();
        for (Annotation annotation : baseAnnotationList) {
            addMetaRequestAnnotation(annotation);
        }
    }

    private void processInterfaceMethods() {
        Annotation[] annotations = method.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation ann = annotations[i];
            // 添加自定义注解
            addMetaRequestAnnotation(ann);
        }
        returnClass = method.getReturnType();
    }

    private void addMetaRequestAnnotation(Annotation annotation) {
        Class<? extends Annotation> annType = annotation.annotationType();
        Class<? extends MethodAnnotationLifeCycle> interceptorClass = null;

        MethodLifeCycle methodLifeCycleAnn = annType.getAnnotation(MethodLifeCycle.class);
        if (methodLifeCycleAnn == null) {
            BaseLifeCycle baseLifeCycle = annType.getAnnotation(BaseLifeCycle.class);
            if (baseLifeCycle != null) {
                Class<? extends BaseAnnotationLifeCycle> baseAnnLifeCycleClass = baseLifeCycle.value();
                if (baseAnnLifeCycleClass != null) {
                    if (MethodAnnotationLifeCycle.class.isAssignableFrom(baseAnnLifeCycleClass)) {
                        interceptorClass = (Class<? extends MethodAnnotationLifeCycle>) baseAnnLifeCycleClass;
                    } else {
                        addInterceptor(baseAnnLifeCycleClass);
                        return;
                    }
                }
            }
        }

        if (methodLifeCycleAnn != null || interceptorClass != null) {
            if (interceptorClass == null) {
                interceptorClass = methodLifeCycleAnn.value();
                if (!Interceptor.class.isAssignableFrom(interceptorClass)) {
                    throw new MyhttpInterceptorDefineException(interceptorClass);
                }
            }
            Interceptor interceptor = addInterceptor(interceptorClass);
            if (interceptor instanceof LoggerAnnotationLifeCycle)
                return;
            if (interceptor instanceof MethodAnnotationLifeCycle) {
                processMetaRequest(annotation);
                MethodAnnotationLifeCycle lifeCycle = (MethodAnnotationLifeCycle) interceptor;
                lifeCycle.onMethodInitialized(this, annotation);
            }
        }
    }
    protected MetaRequest createMetaRequest(Annotation annotation) {
        MetaRequest metaRequest = new MetaRequest(annotation);
        ReflectUtils.copyAnnotationAttributes(annotation, metaRequest);
        return metaRequest;
    }
    private void processMetaRequest(Annotation annotation) {
        metaRequest = createMetaRequest(annotation);
        Class[] paramTypes = method.getParameterTypes();
        Type[] genericParamTypes = method.getGenericParameterTypes();
        Annotation[][] paramAnns = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();
        String[] dataArray = metaRequest.getData();
        String[] headerArray = ArrayUtils.addAll(metaRequest.getHeaders(),
                                                  baseMetaRequest.getHeaders());
        retryer = configuration.getRetryer();

        try {
            if (Retryer.class.isAssignableFrom(baseMetaRequest.getRetryer()))
                retryer = baseMetaRequest.getRetryer().newInstance();
            if (Retryer.class.isAssignableFrom(metaRequest.getRetryer()))
                retryer = metaRequest.getRetryer().newInstance();
        } catch (Exception e) {

        }
        timeout = configuration.getTimeout();

        if (baseMetaRequest.getTimeout() > 0)
            timeout = baseMetaRequest.getTimeout();
        if (metaRequest.getTimeout() > 0)
            timeout = metaRequest.getTimeout();
        retryCount = configuration.getRetryCount();
        if (baseMetaRequest.getRetryCount() > 0)
            retryCount = baseMetaRequest.getRetryCount();
        if (metaRequest.getRetryCount() > 0) {
            retryCount = metaRequest.getRetryCount();
        }

        maxRetryInterval = configuration.getMaxRetryInterval();
        if (baseMetaRequest.getMaxRetryInterval() > 0)
            maxRetryInterval = baseMetaRequest.getMaxRetryInterval();
        if (metaRequest.getMaxRetryInterval() > 0)
            maxRetryInterval = metaRequest.getMaxRetryInterval();

        loggerLevel = configuration.getLoggerLevel();
        if (baseMetaRequest.getLoggerLevel().ordinal() < loggerLevel.ordinal())
            loggerLevel = baseMetaRequest.getLoggerLevel();
        if (metaRequest.getLoggerLevel().ordinal() < loggerLevel.ordinal())
            loggerLevel = metaRequest.getLoggerLevel();

        urlTemplate = makeTemplate(metaRequest.getUrl());
        //typeTemplate = makeTemplate(metaRequest.getType());
        dataTypeTemplate = makeTemplate(metaRequest.getDataType());
        String charset = metaRequest.getCharset();
        if (StringUtils.isNotBlank(charset))
            charsetTemplate = makeTemplate(metaRequest.getCharset());
        String encode = metaRequest.getContentEncoding();
        if (StringUtils.isNotBlank(encode))
            encodeTemplate = makeTemplate(encode);
        userAgentTemplate = makeTemplate(metaRequest.getUserAgent());

        if (StringUtils.isNotEmpty(metaRequest.getContentType())) {
            contentTypeTemplate = makeTemplate(metaRequest.getContentType());
        }
        dataTemplateArray = new MappingTemplate[dataArray.length];
        for (int j = 0; j < dataArray.length; j++) {
            String data = dataArray[j];
            MappingTemplate dataTemplate = makeTemplate(data);
            dataTemplateArray[j] = dataTemplate;
        }

        headerTemplateArray = new MappingTemplate[headerArray.length];
        for (int j = 0; j < headerArray.length; j++) {
            String header = headerArray[j];
            MappingTemplate headerTemplate = makeTemplate(header);
            headerTemplateArray[j] = headerTemplate;
        }
        parameterTemplateArray = new MappingParameter[paramTypes.length];
        processParameters(parameters, genericParamTypes, paramAnns);
        Class[] interceptorClasses = metaRequest.getInterceptor();
        if (interceptorClasses != null && interceptorClasses.length > 0) {
            for (int cidx = 0, len = interceptorClasses.length; cidx < len; cidx++) {
                Class interceptorClass = interceptorClasses[cidx];
                addInterceptor(interceptorClass);
            }
        }
    }

    private void processParameters(Parameter[] parameters, Type[] genericParamTypes, Annotation[][] paramAnns) {
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Class paramType = param.getType();
            Annotation[] anns = paramAnns[i];
            MappingParameter parameter = new MappingParameter(paramType);
            parameter.setIndex(i);
            parameter.setName(param.getName());
            parameterTemplateArray[i] = parameter;
            processParameterAnnotation(parameter, anns);
        }
    }

    private void processParameterAnnotation(MappingParameter parameter, Annotation[] anns) {
        for (int i = 0; i < anns.length; i++) {
            Annotation ann = anns[i];
            Class annType = ann.annotationType();
            ParamLifeCycle paramLifeCycleAnn = (ParamLifeCycle) annType.getAnnotation(ParamLifeCycle.class);
            if (paramLifeCycleAnn != null) {
                Class<? extends ParameterAnnotationLifeCycle> interceptorClass = paramLifeCycleAnn.value();
                if (!Interceptor.class.isAssignableFrom(interceptorClass)) {
                    throw new MyhttpInterceptorDefineException(interceptorClass);
                }
                ParameterAnnotationLifeCycle lifeCycle = addInterceptor(interceptorClass);
                lifeCycle.onParameterInitialized(this, parameter, ann);
            }
        }
    }

    private MyhttpRequest makeRequest(Object[] args) {
        MyhttpRequest<T> request = new MyhttpRequest(configuration, args);
        request.setJsonConverter(jsonConverter)
                .setReturnClass(returnClass)
                .setLogger(logger)
                .setTimeout(timeout)
                .setLoggerLevel(loggerLevel)
                .setCharset(charsetTemplate.render(args))
                .setOptions(new Request.Options(timeout));
        String renderedContentType = null;
        if (baseContentTypeTemplate != null)
            renderedContentType = baseContentTypeTemplate.render(args).trim();
        if (contentTypeTemplate != null) {
            renderedContentType = contentTypeTemplate.render(args).trim();
        }
        String renderedUserAgent = null;
        if (userAgentTemplate != null) {
            renderedUserAgent = userAgentTemplate.render(args).trim();
        }
        String contentEncoding = null;
        if (encodeTemplate != null) {
            contentEncoding = encodeTemplate.render(args);
        }

        if (StringUtils.isNotEmpty(renderedContentType)) {
            request.setContentType(renderedContentType);
        }

        if (StringUtils.isNotEmpty(contentEncoding)) {
            request.setContentEncoding(contentEncoding);
        }

        if (StringUtils.isNotEmpty(renderedUserAgent)) {
            request.setUserAgent(renderedUserAgent);
        }
        MetaRequest baseMetaRequest = interfaceProxyHandler.getBaseMetaRequest();
        String baseUrl = null;
        if (baseUrlTemplate != null) {
            baseUrl = baseUrlTemplate.render(args);
        }
        String renderedUrl = urlTemplate.render(args);
        renderedUrl = URLUtils.getValidURL(baseUrl, renderedUrl);
        URLUtils.MyhttpURL myhttpURL = new URLUtils.MyhttpURL(renderedUrl);
        try {
            URLUtils.urlHandle(myhttpURL, request);
        } catch (MalformedURLException e) {
            throw new MyhttpRuntimeException(e);
        }

        MyhttpRequestType type = type(args);
        request.setProtocol(myhttpURL.getProtocol())
                .setUrl(myhttpURL.getUrl())
                .setType(type);
        String[] headerArray = baseMetaRequest.getHeaders();
        MappingTemplate[] baseHeaders = null;
        if (headerArray != null && headerArray.length > 0) {
            baseHeaders = new MappingTemplate[headerArray.length];
            for (int j = 0; j < baseHeaders.length; j++) {
                MappingTemplate header = new MappingTemplate(headerArray[j], this);
                baseHeaders[j] = header;
            }
        }
        List<RequestNameValue> nameValueList = new ArrayList<>();
        StringBuilder bodyBuilder = new StringBuilder();
        boolean isQueryData = false;
        for (int i = 0; i < dataTemplateArray.length; i++) {
            MappingTemplate dataTemplate = dataTemplateArray[i];
            String data = dataTemplate.render(args);
            bodyBuilder.append(data);
            if (i < dataTemplateArray.length - 1) {
                bodyBuilder.append("&");
            }
            String[] paramArray = data.split("&");
            for (int j = 0; j < paramArray.length; j++) {
                String dataParam = paramArray[j];
                String[] dataNameValue = dataParam.split("=");
                if (dataNameValue.length > 0) {
                    String name = dataNameValue[0].trim();
//                    RequestNameValue nameValue = new RequestNameValue(name, type.isDefaultParamInQuery());
                    RequestNameValue nameValue = new RequestNameValue(name, TARGET_QUERY);
                    if (dataNameValue.length == 2) {
                        isQueryData = true;
                        nameValue.setValue(dataNameValue[1].trim());
                    }
                    nameValueList.add(nameValue);
                    // dataNameValueList.add(nameValue);
                }
            }
        }
        request.addData(nameValueList);
        handleParameters(request);
        if ((type.getDefaultParamTarget() == TARGET_BODY
                || (type.getDefaultParamTarget() == TARGET_QUERY && !isQueryData))
                && bodyBuilder.length() > 0) {
            String requestBody = bodyBuilder.toString();
            request.setRequestBody(requestBody);
        }
        for (int i = 0; i < headerTemplateArray.length; i++) {
            MappingTemplate headerTemplate = headerTemplateArray[i];
            String header = headerTemplate.render(args);
            String[] headNameValue = header.split(":");
            if (headNameValue.length > 0) {
                String name = headNameValue[0].trim();
                RequestNameValue nameValue = new RequestNameValue(name, TARGET_HEADER);
                if (headNameValue.length == 2) {
                    nameValue.setValue(headNameValue[1].trim());
                }
                request.addHeader(nameValue);
            }
        }
        if (globalInterceptorList != null && globalInterceptorList.size() > 0) {
            for (Interceptor item : globalInterceptorList) {
                request.addInterceptor(item);
            }
        }

        if (baseInterceptorList != null && baseInterceptorList.size() > 0) {
            for (Interceptor item : baseInterceptorList) {
                request.addInterceptor(item);
            }
        }

        if (interceptorList != null && interceptorList.size() > 0) {
            for (Interceptor item : interceptorList) {
                request.addInterceptor(item);
            }
        }

        return request;
    }

    private void handleParameters(MyhttpRequest<T> request) {
        Object[] args = request.getArguments();
        MyhttpRequestType type = request.getType();
        List bodyList = request.getBodyList();
        List<RequestNameValue> nameValueList = new ArrayList<>();
        for (int i = 0; i < namedParameters.size(); i++) {
            MappingParameter parameter = namedParameters.get(i);
            if (parameter.isObjectProperties()) {
                int target = parameter.isUnknownTarget() ? type.getDefaultParamTarget() : parameter.getTarget();
                Object obj = args[parameter.getIndex()];
                if (parameter.isJsonParam()) {
                    String json = "";
                    if (obj != null) {
                        obj = parameter.getFilterChain().doFilter(configuration, obj);
                        json = jsonConverter.encode(obj);
                    }
                    if (parameter.isHeader()) {
                        request.addHeader(new RequestNameValue(parameter.getJsonParamName(), json, target));
                    } else {
                        nameValueList.add(new RequestNameValue(parameter.getJsonParamName(), json, target));
                    }
                } else if (!parameter.getFilterChain().isEmpty()) {
                    obj = parameter.getFilterChain().doFilter(configuration, obj);
                    if (parameter.isHeader()) {
                        request.addHeader(new RequestNameValue(null, obj, target));
                    } else {
                        nameValueList.add(new RequestNameValue(null, obj, target));
                    }
                } else if (obj instanceof List
                        || obj.getClass().isArray()
                        || ReflectUtils.isPrimaryType(obj.getClass())) {
                    bodyList.add(obj);
                } else if (obj instanceof Map) {
                    Map map = (Map) obj;
                    for (Object key : map.keySet()) {
                        if (key instanceof CharSequence) {
                            Object value = map.get(key);
                            if (parameter.isHeader()) {
                                request.addHeader(new RequestNameValue(String.valueOf(key), value, target));
                            } else {
                                nameValueList.add(new RequestNameValue(String.valueOf(key), value, target));
                            }

                        }
                    }
                } else {
                    try {
                        List<RequestNameValue> list = getNameValueListFromObjectWithJSON(parameter, obj, type);
                        for (RequestNameValue nameValue : list) {
                            if (nameValue.isInHeader()) {
                                request.addHeader(nameValue);
                            } else {
                                nameValueList.add(nameValue);
                            }
                        }
                    } catch (Throwable th) {
                        throw new MyhttpRuntimeException(th);
                    }
                }
            } else if (parameter.getIndex() != null) {
                int target = parameter.isUnknownTarget() ? type.getDefaultParamTarget() : parameter.getTarget();
                RequestNameValue nameValue = new RequestNameValue(parameter.getName(), target);
                Object obj = args[parameter.getIndex()];
                if (obj != null) {
                    nameValue.setValue(String.valueOf(obj));
                    if (parameter.isHeader()) {
                        request.addHeader(nameValue);
                    } else {
                        nameValueList.add(nameValue);
                    }
                }
            }
        }
        request.addData(nameValueList);
    }

    private List<RequestNameValue> getNameValueListFromObjectWithJSON(MappingParameter parameter, Object obj, MyhttpRequestType type) {
        Map<String, Object> propMap = jsonConverter.convertObjectToMap(obj);
        List<RequestNameValue> nameValueList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : propMap.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                RequestNameValue nameValue = new RequestNameValue(name, value,
                        parameter.isUnknownTarget() ? type.getDefaultParamTarget() : parameter.getTarget());
                nameValueList.add(nameValue);
            }
        }
        return nameValueList;
    }

    private MyhttpRequestType type(Object[] args) {
        String renderedType = typeTemplate.render(args);
        if (StringUtils.isBlank(renderedType)) {
            String typeFromName = methodNameItems[0];
            MyhttpRequestType type = MyhttpRequestType.findType(typeFromName);
            if (type != null) {
                return type;
            }
            return MyhttpRequestType.GET;
        }
        MyhttpRequestType type = MyhttpRequestType.findType(renderedType);
        if (type != null) {
            return type;
        }
        throw new MyhttpRuntimeException("Http request type \"" + renderedType + "\" is not be supported.");
    }

    public void processParameterFilter(MappingParameter parameter, String filterName) {
        if (StringUtils.isNotEmpty(filterName)) {
            String[] filterNameArray = filterName.split(",");
            for (String name : filterNameArray) {
                Filter filter = configuration.newFilterInstance(name);
                parameter.addFilter(filter);
            }
        }
    }

    private <T extends Interceptor> T addInterceptor(Class<T> interceptorClass) {
        if (interceptorList == null) {
            interceptorList = new LinkedList<>();
        }
        if (!Interceptor.class.isAssignableFrom(interceptorClass) || interceptorClass.isInterface()) {
            throw new MyhttpRuntimeException("Class [" + interceptorClass.getName() + "] is not a implement of [" +
                    Interceptor.class.getName() + "] interface.");
        }
        T interceptor = interceptorFactory.getInterceptor(interceptorClass);
        interceptorList.add(interceptor);
        return interceptor;
    }

    public MappingTemplate makeTemplate(String text) {
        return new MappingTemplate(text, this);
    }

    @Override
    public Object getVariableValue(String name) {
        Object value = configuration.getVariableValue(name);
        return value;
    }

    @Override
    public MappingVariable getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public Myhttp.Builder getConfiguration() {
        return this.configuration;
    }

    public Object invoke(Object[] args) throws Throwable {
        MyhttpRequest myhttpRequest = makeRequest(args);
        myhttpRequest.getInterceptorChain().onInvokeMethod(myhttpRequest,this,args);
        Request request = myhttpRequest.create();
        Retryer retryerClone = retryer.clone(retryCount, maxRetryInterval);
        int retryCount = 0;
        for(;;) {
            try {
                return myhttpRequest.execute(request);
            } catch (RetryableException e) {
                try {
                    retryCount = retryerClone.continueRetry(e);
                } catch (RetryableException th) {
                    Throwable cause = th.getCause();
                    if (cause != null) {
                        throw th;
                    } else {
                        return th;
                    }
                }
                if (loggerLevel != Logger.Level.NONE) {
                    logger.log("---> RETRYING "+retryCount+"th");
                }
                continue;
            }
        }
    }
}
