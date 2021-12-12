package com;

import com.iwbfly.client.GetClient;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther: pangyajun
 * @create: 2021/12/8 16:17
 **/
@Component
public class MyListener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //String username = event.getApplicationContext().getBean(GetClient.class).getNew("username", "123456");
        //ConcurrentHashMap
    }
}
