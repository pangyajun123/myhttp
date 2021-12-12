package com.iwbfly.myhttp.beans;

import com.iwbfly.myhttp.Client;
import com.iwbfly.myhttp.Myhttp;

import com.iwbfly.myhttp.converter.json.JsonConverter;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.retry.Retryer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @auther: pangyajun
 * @create: 2021/10/13 23:26
 **/
public class MyhttpClientFactoryBean<T> implements FactoryBean<Object>, InitializingBean, ApplicationContextAware {

    private Class<T> type;
    private Myhttp.Builder configuration;

    private ApplicationContext applicationContext;
    @Override
    public Object getObject() throws Exception {
        configuration = getOrInstantiate(Myhttp.Builder.class);
        Client client = getOrInstantiate(Client.class);
        if(client != null)
            configuration.client(client);
        Retryer retryer = getOrInstantiate(Retryer.class);
        Logger logger = getOrInstantiate(Logger.class);
        if (logger != null)
            configuration.logger(logger);
        JsonConverter jsonConverter = getOrInstantiate(JsonConverter.class);
        if (jsonConverter != null)
            configuration.jsonConverter(jsonConverter);
        return configuration.createInstance(type);
    }

    private <T> T getOrInstantiate(Class<T> tClass) {
        try {
            return this.applicationContext.getBean(tClass);
        }
        catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public Myhttp.Builder getConfiguration() {
        return configuration;
    }

    public MyhttpClientFactoryBean<T> setConfiguration(Myhttp.Builder configuration) {
        this.configuration = configuration;
        return this;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
