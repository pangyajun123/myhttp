package com.iwbfly.http.client;

import com.iwbfly.model.User;
import com.iwbfly.myhttp.annotation.*;
import com.iwbfly.myhttp.http.Response;
import com.iwbfly.myhttp.logging.Logger;

import java.util.Map;


/**
 * @auther: pangyajun
 * @create: 2021/12/4 14:14
 **/
@MyhttpClient(url = "${localhostUrl}",
        headers = {
                "Accept-Charset: UTF-8",
                "Accept: text/plain"
        }
)
public interface GetClient {

    @Get(url = "test?username=${0}&password=${1}",
            headers = {"token:6666666"},
            loggerLevel = Logger.Level.BASIC)
    String get1(String username, String password);

    @Get(url = "test",
            headers = {"token1:${0}", "token2:${1}"},
            loggerLevel = Logger.Level.FULL)
    String get2(@Query("username") String username,
             @Query("password") String password,
             @Header("token") String token);

    @Get(url = "test",
            headers = {"token:666666"},
            data={"data1=${$0.username}&data2=${user.password}"})
   String get3(@Query @Variable("user") User user);

    @Get(url = "test", headers = {"token:666666"})
    String get4(@Query User user);

    @Get(url = "test?username=${user.username}&password=${user.password}",
            headers = {"token:${user.username}"})
    String get5(@Variable("user") User user);

    @Get(url = "test", headers = {"token:666666"})
    Response get6(@Query User user);
}
