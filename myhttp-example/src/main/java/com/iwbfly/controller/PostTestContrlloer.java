package com.iwbfly.controller;

import com.iwbfly.client.PostTestClient;
import com.iwbfly.model.User;
import com.iwbfly.myhttp.converter.json.FastJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class PostTestContrlloer {
    @Autowired
    private PostTestClient postTestClient;

    public PostTestContrlloer() {
    }

    @RequestMapping({"test"})
    @ResponseBody
    Map testPost(HttpServletRequest request, String username, String password) {
        HashMap result = new HashMap();
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String s = (String)headerNames.nextElement();
            result.put(s, request.getHeader(s));
        }

        result.put("username", username);
        result.put("password", password);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping({"testPost1"})
    @ResponseBody
    Map testPost1(HttpServletRequest request, @RequestBody User user) {
        FastJsonConverter fastJsonConverter = new FastJsonConverter();
        Map result = fastJsonConverter.convertObjectToMap(user);
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String s = (String)headerNames.nextElement();
            result.put(s, request.getHeader(s));
        }

        return result;
    }

    @RequestMapping({"testPost2"})
    @ResponseBody
    Map testPost2(HttpServletRequest request, @RequestBody List<User> listUser) {
        HashMap result = new HashMap();
        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String s = (String)headerNames.nextElement();
            result.put(s, request.getHeader(s));
        }

        result.put("userList", listUser);
        return result;
    }

    @RequestMapping({"post"})
    @ResponseBody
    Map post() {
        return this.postTestClient.post("xiaoming", "123456");
    }

    @RequestMapping({"post1"})
    @ResponseBody
    Map post1() {
        User user = new User("xiaoming", "123456");
        return this.postTestClient.post1(user);
    }

    @RequestMapping({"post2"})
    @ResponseBody
    Map post2() {
        User user = new User("xiaoming", "123456");
        User user1 = new User("xiaohua", "123456");
        List list = new ArrayList();
        list.add(user);
        list.add(user1);
        return this.postTestClient.post2(list, "token666");
    }

    @RequestMapping({"post3"})
    @ResponseBody
    Map post3() {
        User user = new User("xiaoming", "123456");
        return this.postTestClient.post3(user);
    }

    @RequestMapping({"post4"})
    @ResponseBody
    Map post4() {
        User user = new User("xiaoming", "123456");
        return this.postTestClient.post4(user);
    }

    @RequestMapping({"post5"})
    @ResponseBody
    Map post5() {
        return this.postTestClient.post5("xiaoming", "123456");
    }

    @RequestMapping({"post6"})
    @ResponseBody
    Map post6() {
        User user = new User("xiaoming", "123456");
        return this.postTestClient.post6(user);
    }
}
