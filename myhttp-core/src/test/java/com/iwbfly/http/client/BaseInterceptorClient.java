package com.iwbfly.http.client;

import com.iwbfly.interceptor.BaseInterceptor;
import com.iwbfly.interceptor.MethodInterceptor;
import com.iwbfly.myhttp.annotation.Body;
import com.iwbfly.myhttp.annotation.MyhttpClient;
import com.iwbfly.myhttp.annotation.Post;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 20:29
 **/
@MyhttpClient(url = "${localhostUrl}",
        interceptor = {BaseInterceptor.class}
)
public interface BaseInterceptorClient {

    @Post(url = "test",interceptor = {MethodInterceptor.class})
    String post1(@Body("username") String username, @Body("password") String password);
}
