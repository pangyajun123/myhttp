package com.iwbfly.controller;

import com.iwbfly.client.GetTestClient;
import com.iwbfly.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class GetTestController {
    @Autowired
    private GetTestClient getTestClient;

    public GetTestController() {
    }

    @RequestMapping({"get"})
    @ResponseBody
    Map get1() {
        return this.getTestClient.get1("xiaoming", "123456");
    }

    @RequestMapping({"get2"})
    @ResponseBody
    Map get2() {
        return this.getTestClient.get2("xiaoming", "123456", "token");
    }

    @RequestMapping({"get3"})
    @ResponseBody
    Map get3() {
        User user = new User("xiaoming", "123456");
        return this.getTestClient.get3(user);
    }

    @RequestMapping({"get4"})
    @ResponseBody
    Map get4() {
        User user = new User("xiaoming", "123456");
        return this.getTestClient.get4(user);
    }

    @RequestMapping({"get5"})
    @ResponseBody
    Map get5() {
        User user = new User("xiaoming", "123456");
        return this.getTestClient.get5(user);
    }
}

