package com.iwbfly.myhttp.springboot;

import com.iwbfly.myhttp.Myhttp;
import com.iwbfly.myhttp.springboot.support.MyhttpClientProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @auther: pangyajun
 * @create: 2021/10/14 13:48
 **/
@Configuration
@EnableConfigurationProperties(MyhttpClientProperties.class)
public class MyhttpClientsConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public Myhttp.Builder myhttpBuilder(MyhttpClientProperties properties) {
        return Myhttp.builder()
                .charset(properties.getCharset())
                .retryCount(properties.getRetryCount())
                .maxRetryInterval(properties.getMaxRetryInterval())
                .retryer(properties.getRetryer())
                .cacheEnabled(properties.isCacheEnabled())
                .variables(properties.getVariables())
                .setLoggerLevel(properties.getLoggerLevel())
                .timeout(properties.getTimeout())
                .defaultHeaders(properties.getDefaultHeaders())
                .defaultParameters(properties.getDefaultParameters())
                .interceptors(properties.getInterceptors());
    }

}
