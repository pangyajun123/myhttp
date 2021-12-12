package com.iwbfly.controller;

import com.iwbfly.client.AmapClient;
import com.iwbfly.model.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/12/2 12:36
 **/
@Controller
public class AmapController{
    @Autowired
    private AmapClient amapClient;

    @RequestMapping("getLocation")
    @ResponseBody
    Map getLocation(){
        return amapClient.getLocation("121.475078", "31.223577");
    }

    @RequestMapping("getLocationQuery")
    @ResponseBody
    Map getLocationQuery(){
        return amapClient.getLocationQuery("121.475078", "31.223577");
    }

    @RequestMapping("getLocationQueryBody")
    @ResponseBody
    Map getLocationQueryBody(){
        return amapClient.getLocationQueryBody(new Coordinate("121.475078", "31.223577"));
    }
}
