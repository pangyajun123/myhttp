package com.iwbfly.myhttp.logging;

/**
 * @auther: pangyajun
 * @create: 2021/12/5 13:09
 **/
public interface LogInterceptor {

    default public void requestLog(Message message){}
    default public void responseLog(Message message){}
}
