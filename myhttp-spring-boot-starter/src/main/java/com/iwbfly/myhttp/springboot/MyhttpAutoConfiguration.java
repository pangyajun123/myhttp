package com.iwbfly.myhttp.springboot;

import com.iwbfly.myhttp.Client;

import com.iwbfly.myhttp.springboot.support.HttpClientProperties;
import com.iwbfly.myhttp.springboot.support.MyhttpClientProperties;
import com.iwbfly.myhttp.http.OkHttpClient;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @auther: pangyajun
 * @create: 2021/10/12 11:46
 **/
@Configuration
@EnableConfigurationProperties({HttpClientProperties.class})
public class MyhttpAutoConfiguration {

    @Configuration
    @ConditionalOnClass(OkHttpClient.class)
    @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
    @ConditionalOnProperty(value = "myhttp.okhttp.enabled",matchIfMissing = true)
    protected static class OkHttpConfiguration {

        private okhttp3.OkHttpClient okHttpClient;

        @Bean
        @ConditionalOnMissingBean(ConnectionPool.class)
        public  ConnectionPool httpClientConnectionPool(
                HttpClientProperties httpClientProperties) {
            Integer maxIdleConnections = httpClientProperties.getMaxConnections();
            Long timeToLive = httpClientProperties.getTimeToLive();
            TimeUnit timeUnit = httpClientProperties.getTimeToLiveUnit();
            return new ConnectionPool(maxIdleConnections, timeToLive, timeUnit);

        }

        @Bean
        public okhttp3.OkHttpClient client(ConnectionPool connectionPool,
                                           HttpClientProperties httpClientProperties) {
            Boolean followRedirects = httpClientProperties.isFollowRedirects();
            Integer connectTimeout = httpClientProperties.getConnectionTimeout();
            Boolean disableSslValidation = httpClientProperties.isDisableSslValidation();
            this.okHttpClient = new okhttp3.OkHttpClient.Builder()
                    .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .followRedirects(followRedirects).connectionPool(connectionPool)
                    .build();
            return this.okHttpClient;
        }

        @PreDestroy
        public void destroy() {
            if (this.okHttpClient != null) {
                this.okHttpClient.dispatcher().executorService().shutdown();
                this.okHttpClient.connectionPool().evictAll();
            }
        }

        @Bean
        @ConditionalOnMissingBean(Client.class)
        public Client myhttpClient(okhttp3.OkHttpClient client) {
            return new OkHttpClient(client);
        }

    }

}
