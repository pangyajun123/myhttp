package com.iwbfly.myhttp.proxy;

import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.annotation.BaseLifeCycle;
import com.iwbfly.myhttp.annotation.MethodLifeCycle;
import com.iwbfly.myhttp.interceptor.InterceptorFactory;
import com.iwbfly.myhttp.lifecycles.BaseAnnotationLifeCycle;
import com.iwbfly.myhttp.reflection.MetaRequest;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.ReflectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/10/14 13:52
 **/
public class InterfaceProxyHandler<T> implements InvocationHandler {

    private Myhttp.Builder configuration;
    private ProxyFactory proxyFactory;

    private Class<T> interfaceClass;

    private Map<Method, MyhttpMethod> myhttpMethodMap = new HashMap<>();

    private MetaRequest baseMetaRequest;

    private InterceptorFactory interceptorFactory;


    private List<Annotation> baseAnnotations = new LinkedList<>();

    public InterfaceProxyHandler( Myhttp.Builder configuration,ProxyFactory proxyFactory, Class<T> interfaceClass) {
        this.configuration = configuration;
        this.proxyFactory = proxyFactory;
        this.interfaceClass = interfaceClass;
        this.interceptorFactory = configuration.getInterceptorFactory();
        prepareBaseInfo();
        initMethods();
    }
    protected MetaRequest createMetaRequest(Annotation annotation) {
        MetaRequest metaRequest = new MetaRequest(annotation);
        ReflectUtils.copyAnnotationAttributes(annotation, metaRequest);
        return metaRequest;

    }
    private void prepareBaseInfo() {
        Annotation[] annotations = interfaceClass.getAnnotations();
        for (Annotation annotation : annotations) {
            BaseLifeCycle baseLifeCycle = annotation.annotationType().getAnnotation(BaseLifeCycle.class);
                if (baseLifeCycle != null) {
                    if (baseMetaRequest == null)
                    baseMetaRequest = createMetaRequest(annotation);
                    Class<? extends BaseAnnotationLifeCycle> interceptorClass = baseLifeCycle.value();
                    if (interceptorClass != null) {
                        BaseAnnotationLifeCycle baseInterceptor = interceptorFactory.getInterceptor(interceptorClass);
                        baseInterceptor.onProxyHandlerInitialized(this, annotation);
                    }
                }
                baseAnnotations.add(annotation);

        }
    }
    private void initMethods() {
        Method[] methods = interfaceClass.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Annotation[] annotations = method.getAnnotations();
            boolean make = false;
            for (int j = 0; j < annotations.length; j++) {
                Annotation ann = annotations[j];
                Class<? extends Annotation> annType = ann.annotationType();
                if(annType.getAnnotation(MethodLifeCycle.class) == null) {
                    make = false;
                    continue;
                }
                make = true;
                break;
            }
            if(!make)
                continue;
            MyhttpMethod myhttpMethod = new MyhttpMethod(this,configuration,method);
            myhttpMethodMap.put(method, myhttpMethod );
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("toString") && (args == null || args.length == 0)) {
            return "{Myhttp Proxy Object of " + interfaceClass.getName() + "}";
        }
        if (methodName.equals("equals") && (args != null && args.length == 1)) {
            Object obj = args[0];
            if (Proxy.isProxyClass(obj.getClass())) {
                InvocationHandler h1 = Proxy.getInvocationHandler(proxy);
                InvocationHandler h2 = Proxy.getInvocationHandler(obj);
                return h1.equals(h2);
            }
            return false;
        }
        MyhttpMethod forestMethod = myhttpMethodMap.get(method);
        return forestMethod.invoke(args);
    }

    public ProxyFactory getProxyFactory() {
        return proxyFactory;
    }

    public void setProxyFactory(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Map<Method, MyhttpMethod> getMyhttpMethodMap() {
        return myhttpMethodMap;
    }

    public void setMyhttpMethodMap(Map<Method, MyhttpMethod> myhttpMethodMap) {
        this.myhttpMethodMap = myhttpMethodMap;
    }

    public MetaRequest getBaseMetaRequest() {
        return baseMetaRequest;
    }

    public void setBaseMetaRequest(MetaRequest baseMetaRequest) {
        this.baseMetaRequest = baseMetaRequest;
    }

    public Myhttp.Builder getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Myhttp.Builder configuration) {
        this.configuration = configuration;
    }

    public InterceptorFactory getInterceptorFactory() {
        return interceptorFactory;
    }

    public void setInterceptorFactory(InterceptorFactory interceptorFactory) {
        this.interceptorFactory = interceptorFactory;
    }

    public List<Annotation> getBaseAnnotations() {
        return baseAnnotations;
    }

    public void setBaseAnnotations(List<Annotation> baseAnnotations) {
        this.baseAnnotations = baseAnnotations;
    }
}
