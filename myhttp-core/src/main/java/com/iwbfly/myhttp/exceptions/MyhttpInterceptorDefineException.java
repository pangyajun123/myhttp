package com.iwbfly.myhttp.exceptions;

/**
 * @auther: pangyajun
 * @create: 2021/11/28 18:29
 **/
public class MyhttpInterceptorDefineException extends MyhttpRuntimeException{

    public MyhttpInterceptorDefineException(Class clazz) {
        super("[myhttp] Interceptor class \"" + clazz.getName() + "\" cannot be initialized, because interceptor class must implements com.iwbfly.myhttp.interceptor.Interceptor");
    }
}
