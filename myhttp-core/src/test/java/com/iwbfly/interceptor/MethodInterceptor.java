package com.iwbfly.interceptor;

import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.utils.RequestNameValue;

import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_BODY;
import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_QUERY;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 20:34
 **/
public class MethodInterceptor implements Interceptor {

    @Override
    public void onInvokeMethod(MyhttpRequest request, MyhttpMethod method, Object[] args) {
        request.addData(new RequestNameValue("methodOnInvokeMethodAdd","666666",TARGET_BODY));
        request.addHeader("methodOnInvokeMethodHeader","666666");
    }
    @Override
    public boolean beforeExecute(MyhttpRequest request) {
        request.addData(new RequestNameValue("methodBeforeExecuteAdd","666666",TARGET_BODY));
        request.addHeader("methodBeforeExecuteHeader","666666");
        return true;
    }
}
