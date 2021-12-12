package com.iwbfly.myhttp.annotation;


import com.iwbfly.myhttp.lifecycles.param.VariableLifeCycle;

import java.lang.annotation.*;

@Documented
@ParamLifeCycle(VariableLifeCycle.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface Variable {

    String value() ;
}
