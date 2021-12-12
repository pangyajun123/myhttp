package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

import java.lang.annotation.Annotation;

public class MappingIndex extends MappingExpr {

    private final Integer index;

    public MappingIndex(Integer index) {
        super(Token.INDEX);
        this.index = index;
    }

    @Override
    public Object render(Object[] args) {
        return args[index];
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "[Index: " + index + "]";
    }

    /**
     * 参数注解的生命周期
     * @param <A> 注解类
     * @param <I> 返回类型
     * @author gongjun[dt_flys@hotmail.com]
     * @since 2020-08-21 0:22
     */
    public static interface ParameterAnnotationLifeCycle<A extends Annotation, I> extends Interceptor<I> {

        /**
         * 在被注解修饰的方法参数初始化时被调用
         * @param method
         * @param parameter
         * @param annotation
         */
        void onParameterInitialized(MyhttpMethod method, MappingParameter parameter, A annotation);

        @Override
        default void onSuccess(I data, MyhttpRequest request, MyhttpResponse response) {

        }

        @Override
        default void onError(MyhttpRuntimeException ex, MyhttpRequest request, MyhttpResponse response) {

        }
    }
}
