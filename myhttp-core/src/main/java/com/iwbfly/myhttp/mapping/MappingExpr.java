package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.config.VariableScope;

public abstract class MappingExpr {

    final Token token;

    protected VariableScope variableScope;

    protected MappingExpr(Token token) {
        this.token = token;
    }

    public Object render(Object[] args) {
        return null;
    }
}
