package com.iwbfly.interceptor;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;

/**
 * @auther: pangyajun
 * @create: 2021/12/3 09:16
 **/
public class TestInterceptor implements Interceptor {

    @Override
    public void onInvokeMethod(MyhttpRequest request, MyhttpMethod method, Object[] args) {
        System.out.println("InvokeMethod-----------------");
    }
    @Override
    public boolean beforeExecute(MyhttpRequest request) {
        System.out.println("beforeExecute-------------------");
        return true;
    }
}
