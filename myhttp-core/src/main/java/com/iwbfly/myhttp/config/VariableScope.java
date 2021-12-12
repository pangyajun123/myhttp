package com.iwbfly.myhttp.config;

import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.mapping.MappingVariable;

public interface VariableScope {

    Object getVariableValue(String name);

    MappingVariable getVariable(String name);

    Myhttp.Builder getConfiguration();
}
