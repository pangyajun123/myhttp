package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.config.VariableScope;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MappingInvoke extends MappingDot {

    private List<MappingExpr> argList;

    public MappingInvoke(VariableScope variableScope, MappingExpr left, MappingIdentity name, List<MappingExpr> argList) {
        this(Token.INVOKE, variableScope, left, name, argList);
    }

    protected MappingInvoke(Token token, VariableScope variableScope, MappingExpr left, MappingIdentity name, List<MappingExpr> argList) {
        super(token, variableScope, left, name);
        this.argList = argList;
    }

    public List<MappingExpr> getArgList() {
        return argList;
    }

    @Override
    public Object render(Object[] args) {
        Object obj = left.render(args);
        String methodName = right.getName();
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            Object result = null;
            if (argList == null || argList.isEmpty()) {
                result = method.invoke(obj);
            }
            else {
                Object[] renderArgs = new Object[argList.size()];
                for (int i = 0, len = argList.size(); i < len; i++) {
                    MappingExpr expr = argList.get(i);
                    renderArgs[i] = expr.render(args);
                }
                result = method.invoke(obj, renderArgs);
            }
            return result;
        } catch (NoSuchMethodException e) {
            throw new MyhttpRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new MyhttpRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new MyhttpRuntimeException(e);
        }

    }

    @Override
    public String toString() {
        return "[Invoke: " + left.toString() + "." + right + " ()]";
    }
}
