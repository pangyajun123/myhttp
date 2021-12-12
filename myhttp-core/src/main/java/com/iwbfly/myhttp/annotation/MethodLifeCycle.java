package com.iwbfly.myhttp.annotation;


import com.iwbfly.myhttp.lifecycles.MethodAnnotationLifeCycle;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MethodLifeCycle {
    Class<? extends MethodAnnotationLifeCycle> value();
}
