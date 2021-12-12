package com.iwbfly.client;

import com.iwbfly.interceptor.TestInterceptor;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.annotation.*;
import com.iwbfly.myhttp.lifecycles.logger.LoggerFilterLifeCycle;
import com.iwbfly.myhttp.logging.Logger;
import com.iwbfly.myhttp.retry.Retryer;

import java.util.List;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/12/1 21:53
 **/
@MyhttpClient(url = "${localhostUrl}"
        , contentType = "application/json")
public interface PostTestClient {

    @Post(url = "test",
            contentType = "application/x-www-form-urlencoded",
            loggerLevel = Logger.Level.FULL,
            timeout = 2000,
            retryCount = 6,
            maxRetryInterval = 800,
            retryer = Retryer.Default.class,
            interceptor = {TestInterceptor.class}
    )
    @LoggerFilter
    Map post(@Body("username") String username, @Body("password") String password);

    @Post(url = "test",
            data = "username=${$0.username}&password=${$0.password}",
            loggerLevel = Logger.Level.NONE)
    Map post1(User user);

    @Post(url = "testPost2")
    @LoggerFilter
    Map post2(@Body List<User> userList, @Header("token") String token);

    @Post(url = "testPost1",
            data = "{\"username\":\"${$0.username}\",\"password\":\"${$0.password}\"}",
            interceptor = LoggerFilterLifeCycle.class
    )
    Map post3(User user);

    @Post(url = "testPost1",
            loggerLevel = Logger.Level.BASIC,
            headers = {"token:123456"})
    @LoggerFilter
    Map post4(@Body User user);

    @Post(url = "test",
            contentType = "application/x-www-form-urlencoded",
            loggerLevel = Logger.Level.BASIC,
            data = "username=${$0}&password=${1}"
    )
    Map post5(String username, String password);

    @Post(url = "testPost1",
            data = "{\"username\":\"${user.username}\",\"password\":\"${user.password}\"}"
    )
    Map post6(@Variable("user") User user);
}
