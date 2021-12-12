package com;

import com.iwbfly.myhttp.EnableMyhttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @auther: pangyajun
 * @create: 2021/10/13 14:16
 **/
@SpringBootApplication
@EnableMyhttpClients("com.iwbfly.client")
public class MyhttpTest {
    public static void main(String[] args) {
        SpringApplication.run(MyhttpTest.class);
    }
}
