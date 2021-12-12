package com.iwbfly.myhttp.lifecycles.param;

import com.iwbfly.myhttp.annotation.Query;
import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.mapping.MappingVariable;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.StringUtils;

import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_QUERY;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 11:19
 **/
public class QueryLifeCycle implements ParameterAnnotationLifeCycle<Query, Object> {

    @Override
    public void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, Query annotation) {
        String name = annotation.value();
        String filterName = annotation.filter();
        if (StringUtils.isNotEmpty(name)) {
            parameter.setName(name);
            MappingVariable variable = new MappingVariable(name, parameter.getType());
            method.processParameterFilter(variable, filterName);
            variable.setIndex(parameter.getIndex());
            method.addVariable(annotation.value(), variable);
            parameter.setObjectProperties(false);
        } else {
            parameter.setObjectProperties(true);
        }
        method.processParameterFilter(parameter, filterName);
        parameter.setTarget(TARGET_QUERY);
        method.addNamedParameter(parameter);
    }
}

