package com.iwbfly.myhttp.lifecycles.method;

import com.iwbfly.myhttp.lifecycles.MethodAnnotationLifeCycle;
import com.iwbfly.myhttp.reflection.MetaRequest;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.ReflectUtils;

import java.lang.annotation.Annotation;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 10:29
 **/
public class RequestLifeCycle implements MethodAnnotationLifeCycle<Annotation, Object> {



    @Override
    public void onMethodInitialized(MyhttpMethod method, Annotation annotation) {
    }

}