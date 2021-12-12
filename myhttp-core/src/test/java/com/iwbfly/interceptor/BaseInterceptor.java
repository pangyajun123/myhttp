package com.iwbfly.interceptor;

import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.utils.RequestNameValue;
import org.apache.logging.slf4j.Log4jLogger;
import org.slf4j.LoggerFactory;

import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_BODY;
import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_QUERY;

/**
 * @auther: pangyajun
 * @create: 2021/12/5 19:23
 **/
public class BaseInterceptor  implements Interceptor {

    public void onInvokeMethod(MyhttpRequest request, MyhttpMethod method, Object[] args) {
        request.addData(new RequestNameValue("baseOnInvokeMethodAdd","666666",TARGET_BODY));
        request.addHeader("baseOnInvokeMethodHeader","666666");
    }

    public boolean beforeExecute(MyhttpRequest request) {
        request.addData(new RequestNameValue("baseBeforeExecuteAdd","666666",TARGET_BODY));
        request.addHeader("baseBeforeExecuteHeader","666666");
        return true;
    }
}
