package com.iwbfly.myhttp.lifecycles.param;

import com.iwbfly.myhttp.annotation.Variable;
import com.iwbfly.myhttp.lifecycles.ParameterAnnotationLifeCycle;
import com.iwbfly.myhttp.mapping.MappingParameter;
import com.iwbfly.myhttp.mapping.MappingVariable;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.StringUtils;

/**
 * @auther: pangyajun
 * @create: 2021/12/2 11:58
 **/
public class VariableLifeCycle implements ParameterAnnotationLifeCycle<Variable, Object> {

    @Override
    public void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, Variable annotation) {
        String name = annotation.value();
        if (StringUtils.isEmpty(name)) {
            name = parameter.getName();
        }
        MappingVariable variable = new MappingVariable(name, parameter.getType());
        variable.setIndex(parameter.getIndex());
        method.addVariable(name, variable);
    }
}
