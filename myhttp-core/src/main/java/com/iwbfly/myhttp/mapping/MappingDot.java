package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.config.VariableScope;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MappingDot extends MappingExpr {

    protected final MappingExpr left;
    protected final MappingIdentity right;

    public MappingDot(VariableScope variableScope, MappingExpr left, MappingIdentity right) {
        this(Token.DOT, variableScope, left, right);
    }

    protected MappingDot(Token token, VariableScope variableScope, MappingExpr left, MappingIdentity right) {
        super(token);
        this.variableScope = variableScope;
        this.left = left;
        this.right = right;
    }


    public Object render(Object[] args) {
        Object obj = left.render(args);
        String getterName = StringUtils.toGetterName(right.getName());
        Method method = null;
        try {
            method = obj.getClass().getDeclaredMethod(getterName);
        } catch (NoSuchMethodException e) {
            try {
                method = obj.getClass().getDeclaredMethod(right.getName());
            } catch (NoSuchMethodException e1) {
                throw new MyhttpRuntimeException(e1);
            }
        }
        if (method == null) {
            throw new MyhttpRuntimeException(new NoSuchMethodException(getterName));
        }
        try {
            Object result = method.invoke(obj);
            return result;
        } catch (InvocationTargetException e) {
            throw new MyhttpRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new MyhttpRuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "[Dot: " + left.toString() + "." + right + "]";
    }
}
