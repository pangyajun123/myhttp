package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.mapping.MappingIndex;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ParamLifeCycle {
    Class<? extends ParameterAnnotationLifeCycle> value();
}
