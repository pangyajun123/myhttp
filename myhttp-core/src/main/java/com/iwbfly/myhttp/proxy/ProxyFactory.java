package com.iwbfly.myhttp.proxy;

import com.iwbfly.myhttp.Myhttp;

import java.lang.reflect.Proxy;

/**
 * @auther: pangyajun
 * @create: 2021/10/14 13:47
 **/
public class ProxyFactory<T> {

    private Myhttp.Builder configuration;
    private Class<T> interfaceClass;

    public ProxyFactory(Myhttp.Builder  configuration, Class<T> interfaceClass) {
        this.configuration = configuration;
        this.interfaceClass = interfaceClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public T createInstance() {
        T instance = (T) configuration.getInstanceCache().get(interfaceClass);
        boolean cacheEnabled = configuration.isCacheEnabled();
        if (cacheEnabled && instance != null) {
            return instance;
        }

        synchronized (configuration.getInstanceCache()) {
            instance = (T) configuration.getInstanceCache().get(interfaceClass);
            if (cacheEnabled && instance != null) {
                return instance;
            }
            InterfaceProxyHandler<T> interfaceProxyHandler = new InterfaceProxyHandler<T>(configuration,this, interfaceClass);
            instance = (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass,MyhttpClientProxy.class}, interfaceProxyHandler);

            if (cacheEnabled) {
                configuration.getInstanceCache().put(interfaceClass, instance);
            }
        }
        return instance;

    }

    public Myhttp.Builder getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Myhttp.Builder configuration) {
        this.configuration = configuration;
    }

}
