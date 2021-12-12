package com.iwbfly.myhttp.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AliasFor {

    /**
     * 属性所对应的别名
     */
    String value();
}
