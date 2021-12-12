package com.iwbfly.myhttp.interceptor;

import com.iwbfly.myhttp.callback.OnError;
import com.iwbfly.myhttp.callback.OnSuccess;
import com.iwbfly.myhttp.logging.LogInterceptor;
import com.iwbfly.myhttp.logging.Message;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 12:46
 **/
public interface Interceptor<T> extends OnSuccess<T>, OnError, LogInterceptor {

    default void onInvokeMethod(MyhttpRequest request, MyhttpMethod method, Object[] args) {
    }

    default boolean beforeExecute(MyhttpRequest request) {
        return true;
    }

    default void afterExecute(MyhttpRequest request, MyhttpResponse response) {
    }

}
