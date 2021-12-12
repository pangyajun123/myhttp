package com.iwbfly.http.client;

/**
 * @auther: pangyajun
 * @create: 2021/12/6 21:30
 **/

import com.iwbfly.myhttp.annotation.Body;
import com.iwbfly.myhttp.annotation.MyhttpClient;
import com.iwbfly.myhttp.annotation.Post;
import com.iwbfly.myhttp.retry.Retryer;

@MyhttpClient(url = "${localhostUrl}")
public interface RetryClient {

    @Post(url = "test",
            retryer = Retryer.Default.class,
            contentType = "application/json",
            timeout = 300,
            retryCount = 6,
            maxRetryInterval = 100
     )
    String post1(@Body("username") String username, @Body("password") String password);
}
