package com.iwbfly.myhttp.interceptor;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.lifecycles.LoggerAnnotationLifeCycle;
import com.iwbfly.myhttp.logging.Message;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 14:33
 **/
public class InterceptorChain implements Interceptor {

    private LinkedList<Interceptor> interceptors = new LinkedList<>();

    public synchronized InterceptorChain addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public int getInterceptorSize() {
        return interceptors.size();
    }

    @Override
    public void onInvokeMethod(MyhttpRequest request, MyhttpMethod method, Object[] args) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor item = iter.next();
            item.onInvokeMethod(request, method, args);
        }
    }


    @Override
    public boolean beforeExecute(MyhttpRequest request) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor item = iter.next();
            boolean result = item.beforeExecute(request);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onSuccess(Object data, MyhttpRequest request, MyhttpResponse response) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor item = iter.next();
            if (response != null) {
                data = response.getResult();
            }
            item.onSuccess(data, request, response);
        }
    }

    @Override
    public void onError(MyhttpRuntimeException ex, MyhttpRequest request, MyhttpResponse response) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor item = iter.next();
            item.onError(ex, request, response);
        }
    }


    @Override
    public void afterExecute(MyhttpRequest request, MyhttpResponse response) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor item = iter.next();
            item.afterExecute(request, response);
        }
    }

    @Override
    public void requestLog(Message message) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor interceptor = iter.next();
            if (LoggerAnnotationLifeCycle.class.isAssignableFrom(interceptor.getClass()))
                interceptor.requestLog(message);
        }
    }

    @Override
    public void responseLog(Message message) {
        Iterator<Interceptor> iter = interceptors.iterator();
        for (; iter.hasNext(); ) {
            Interceptor interceptor = iter.next();
            if (LoggerAnnotationLifeCycle.class.isAssignableFrom(interceptor.getClass()))
                interceptor.responseLog(message);
        }
    }
}