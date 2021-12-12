package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.logger.LoggerFilterLifeCycle;

import java.lang.annotation.*;

@Documented
@MethodLifeCycle(LoggerFilterLifeCycle.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface LoggerFilter {
}
