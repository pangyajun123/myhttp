package com.iwbfly.client;

import com.iwbfly.interceptor.TestInterceptor;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.annotation.*;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.retry.Retryer;

import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/10/13 14:20
 **/
@MyhttpClient(url = "${localhostUrl}",
        headers = {"auth: 123456"},
        retryer = Retryer.Default.class,
        interceptor = TestInterceptor.class)
public interface GetTestClient {

    @Get(url = "test?username=${0}&password=${1}",
            headers = {"token:6666666"},
            loggerLevel = Logger.Level.BASIC)
    Map get1(String username, String password);

    @Get(url = "test",
            headers = {"token1:${0}", "token2:${1}"},
            loggerLevel = Logger.Level.NONE)
    Map get2(@Query("username") String username,
             @Query("password") String password,
             @Header("token") String token);

    @Get(url = "test?username=${0}&password=${1}", headers = {"token:6666666"})
    Map get3(@Query User user);

    @Get(url = "test", headers = {"token:6666666"})
    Map get4(@Query User user);

    @Get(url = "test?username=${user.username}&password=${user.password}", headers = {"token:6666666"})
    Map get5(@Variable("user") User user);


}
