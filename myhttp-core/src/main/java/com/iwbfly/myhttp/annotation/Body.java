package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.param.BodyLifeCycle;

import java.lang.annotation.*;

@Documented
@ParamLifeCycle(BodyLifeCycle.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Body {

    String value() default "";

    String filter() default "";
}
