package com.iwbfly.myhttp.lifecycles.param;

import com.iwbfly.myhttp.annotation.Body;
import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.mapping.MappingVariable;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.StringUtils;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 11:07
 **/
public class BodyLifeCycle implements ParameterAnnotationLifeCycle<Body, Object> {

    @Override
    public void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, Body annotation) {
        String name = annotation.value();
        String filterName = annotation.filter();
        if (StringUtils.isNotEmpty(name)) {
            parameter.setName(name);
            MappingVariable variable = new MappingVariable(name, parameter.getType());
            variable.setIndex(parameter.getIndex());
            method.addVariable(name, variable);
            parameter.setObjectProperties(false);
        } else {
            parameter.setObjectProperties(true);
        }
        method.processParameterFilter(parameter, filterName);
        parameter.setTarget(MappingParameter.TARGET_BODY);
        method.addNamedParameter(parameter);
    }
}
