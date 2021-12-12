package com.iwbfly.myhttp.lifecycles;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

import java.lang.annotation.Annotation;

/**
 * 方法注解的生命周期
 * @param <A> 注解类
 * @param <I> 返回类型
 */
public interface MethodAnnotationLifeCycle<A extends Annotation, I> extends Interceptor<I> {

    default void onMethodInitialized(MyhttpMethod method, A annotation){};

    @Override
    default void onError(MyhttpRuntimeException ex, MyhttpRequest request, MyhttpResponse response) {

    }

    @Override
    default void onSuccess(I data, MyhttpRequest request, MyhttpResponse response) {

    }
}
