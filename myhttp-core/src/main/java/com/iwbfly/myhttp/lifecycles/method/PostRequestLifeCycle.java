package com.iwbfly.myhttp.lifecycles.method;

import com.iwbfly.myhttp.HttpMethod;
import com.iwbfly.myhttp.mapping.MappingTemplate;
import com.iwbfly.myhttp.reflection.MetaRequest;
import com.iwbfly.myhttp.reflection.MyhttpMethod;

import java.lang.annotation.Annotation;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 10:37
 **/
public class PostRequestLifeCycle extends RequestLifeCycle {

    @Override
    public void onMethodInitialized(MyhttpMethod method, Annotation annotation) {
        method.setTypeTemplate(method.makeTemplate(HttpMethod.POST.value()));
    }
}