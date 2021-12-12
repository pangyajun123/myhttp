package com.iwbfly.http.client;

import com.iwbfly.model.User;
import com.iwbfly.myhttp.annotation.*;
import com.iwbfly.myhttp.logging.Logger;


import java.util.List;

/**
 * @auther: pangyajun
 * @create: 2021/11/11 19:22
 **/
@MyhttpClient(url = "${localhostUrl}",
        headers = {"Accept-Charset: UTF-8",
                "Accept: text/plain"},
        contentType = "application/json"
)
public interface PostClient {

    @Post(url = "test")
    String post1(@Body("username") String username, @Body("password") String password);

    @Post(url = "test",
            data = "username=${$0.username}&password=${$0.password}")
    String post2(User user);

    @Post(url = "test")
    String post3(@Body List<User> userList, @Header("token") String token);

    @Post(url = "test",
            data = "{\"username\":\"${$0.username}\",\"password\":\"${$0.password}\"}"
    )
    String post4(User user);

    @Post(url = "test",
            loggerLevel = Logger.Level.BASIC,
            headers = {"token:666666"})
    String post5(@Body User user);

    @Post(url = "test",
            contentType = "application/x-www-form-urlencoded",
            loggerLevel = Logger.Level.BASIC,
            data = "username=${$0}&password=${1}"
    )
    String post6(String username, String password);

    @Post(url = "test",
            data = "{\"username\":\"${user.username}\",\"password\":\"${user.password}\"}",
            headers = {"token1:${1}","token2:${user.password}"}
    )
    String post7(@Variable("user") User user,String token1,@Header("token3")String token3);


}
