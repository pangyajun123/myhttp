package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.BaseAnnotationLifeCycle;

import java.lang.annotation.*;

/**
 * Life Cycle Class Annotation
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface BaseLifeCycle {
    Class<? extends BaseAnnotationLifeCycle> value();
}
