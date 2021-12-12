package com.iwbfly.myhttp.annotation;

import com.iwbfly.myhttp.lifecycles.param.QueryLifeCycle;
import java.lang.annotation.*;

@Documented
@ParamLifeCycle(QueryLifeCycle.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Query {

    /**
     * URL query name
     * @return
     */
    String value() default "";

    /**
     * The filters will do some processing for the query value before sending request.
     * @return
     */
    String filter() default "";
}
