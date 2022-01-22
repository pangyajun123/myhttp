# myhttp

#### 介绍
基于http协议api调用框架


#### 快速开始

基于springboot

##### 第一步：添加maven依赖

添加maven依赖
```xml
 <dependency>
     <groupId>com.iwbfly</groupId>
     <artifactId>myhttp-spring-boot-starter</artifactId>
     <version>0.0.1-SNAPSHOT</version>
</dependency>
```
##### 第二步：创建interfate

高德地图api

```java
package com.iwbfly.client;

import com.iwbfly.myhttp.annotation.Get;
import com.iwbfly.myhttp.annotation.MyhttpClient;

@MyhttpClient(url = "http://ditu.amap.com")
public interface AmapClient {

     /**
     * @Get注解代表该方法专做GET请求
     * 在url中的${0}代表引用第一个参数，${1}引用第二个参数
     */
    @Get(url = "service/regeo?longitude=${0}&latitude=${1}")
    Map getLocation(String longitude, String latitude);
}
```
##### 第三步：扫描接口

在Spring Boot的配置类或者启动类上加上@EnableMyhttpClients注解，并在属性里填上远程接口的所在的包名

```java
package com;

import com.iwbfly.myhttp.EnableMyhttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMyhttpClients("com.iwbfly.client")
public class MyhttpTest {
    public static void main(String[] args) {
        SpringApplication.run(MyhttpTest.class);
    }
}
``` 
##### 第四步：调用接口

接下来就可以愉快的调用接口了

```java
// 注入接口实例
@Autowired
private AmapClient amapClient;
...
// 调用接口
Map result = amapClient.getLocation("121.475078", "31.223577");
System.out.println(result);

```
#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
