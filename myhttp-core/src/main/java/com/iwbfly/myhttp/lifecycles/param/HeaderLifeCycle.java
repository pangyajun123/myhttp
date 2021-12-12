package com.iwbfly.myhttp.lifecycles.param;

import com.iwbfly.myhttp.annotation.Header;
import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.mapping.MappingVariable;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.StringUtils;

public class HeaderLifeCycle implements ParameterAnnotationLifeCycle<Header, Object> {

    @Override
    public void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, Header annotation) {
        String name = annotation.value();
        if (StringUtils.isNotEmpty(name)) {
            parameter.setName(name);
            MappingVariable variable = new MappingVariable(name, parameter.getType());
            variable.setIndex(parameter.getIndex());
            method.addVariable(name, variable);
            parameter.setObjectProperties(false);
        } else {
            parameter.setObjectProperties(true);
        }
        parameter.setTarget(MappingParameter.TARGET_HEADER);
        method.addNamedParameter(parameter);
    }
}
