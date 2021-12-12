package com.iwbfly.myhttp.interceptor;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.reflection.MyhttpResponse;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 12:37
 **/
public interface ErrorHandler {

    void handleError(MyhttpRuntimeException myhttpRuntimeException, MyhttpRequest myhttpRequest, MyhttpResponse myhttpResponse);
}
