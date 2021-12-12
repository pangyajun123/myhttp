package com.iwbfly.myhttp.lifecycles;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

import java.lang.annotation.Annotation;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 10:10
 **/
public interface ParameterAnnotationLifeCycle<A extends Annotation, I> extends Interceptor<I> {

    default void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, A annotation){};
    default void onInvokeParameter(MyhttpMethod method, MappingParameter parameter, A annotation){};

}