package com.iwbfly.interceptor;

import com.iwbfly.myhttp.interceptor.Interceptor;
import com.iwbfly.myhttp.lifecycles.LoggerAnnotationLifeCycle;
import com.iwbfly.myhttp.logging.Message;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 21:19
 **/
public class LoggerIntercepter implements LoggerAnnotationLifeCycle {

    @Override
    public void requestLog(Message message){
        System.out.println("------LoggerIntercepter requestLog----");
        String str = message.getMessage();
        if (str != null)
            str = str.replaceAll("\"password\":\"(\\w+)\"", "\"password\":\"******\"");
        message.setMessage(str);
    }
    @Override
    public void responseLog(Message message){
        System.out.println("------LoggerIntercepter responseLog----");
    }
}
