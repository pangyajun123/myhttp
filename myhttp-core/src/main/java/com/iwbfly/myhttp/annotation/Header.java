package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.param.HeaderLifeCycle;

import java.lang.annotation.*;

@Documented
@ParamLifeCycle(HeaderLifeCycle.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Header {

    String value() default "";
}
