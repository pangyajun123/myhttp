package com.iwbfly.myhttp.lifecycles;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.proxy.InterfaceProxyHandler;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

import java.lang.annotation.Annotation;

/**
 * @author gongjun[dt_flys@hotmail.com]
 * @since 2020-08-23 23:04
 */
public interface BaseAnnotationLifeCycle<A extends Annotation, I> extends Interceptor<I> {

    default void onProxyHandlerInitialized(InterfaceProxyHandler interfaceProxyHandler, A annotation) {
    }
}
