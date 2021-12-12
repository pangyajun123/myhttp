package com.iwbfly.http.client;

import com.iwbfly.interceptor.BaseInterceptor;
import com.iwbfly.interceptor.LoggerIntercepter;
import com.iwbfly.interceptor.MethodInterceptor;
import com.iwbfly.myhttp.annotation.Body;
import com.iwbfly.myhttp.annotation.LoggerFilter;
import com.iwbfly.myhttp.annotation.MyhttpClient;
import com.iwbfly.myhttp.annotation.Post;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 20:30
 **/
@MyhttpClient(url = "${localhostUrl}",
        interceptor = {LoggerIntercepter.class}
)
public interface LoggerFilterClient {

    @Post(url = "test",contentType = "application/json")
    @LoggerFilter
    String post1(@Body("username") String username, @Body("password") String password);

}
