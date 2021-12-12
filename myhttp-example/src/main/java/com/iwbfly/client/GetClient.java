package com.iwbfly.client;

import com.iwbfly.myhttp.annotation.Get;
import com.iwbfly.myhttp.annotation.MyhttpClient;

/**
 * @auther: pangyajun
 * @create: 2021/12/5 00:27
 **/
@MyhttpClient(url = "${localhostUrl}",
        headers = {
                "Accept-Charset: UTF-8",
                "Accept: text/plain"
        },
        userAgent = "${userAgent}")
public interface GetClient {

    @Get(url = "test?username=${0}&password=${1}")
    String getNew(String username, String password);
}
