package com.iwbfly.myhttp.springboot.support;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 21:10
 **/
@ConfigurationProperties("myhttp.httpcleint")
public class HttpClientProperties {

    /**
     * Default value for disabling SSL validation.
     */
    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;

    /**
     * Default value for max number od connections.
     */
    public static final int DEFAULT_MAX_CONNECTIONS = 200;

    /**
     * Default value for max number od connections per route.
     */
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

    /**
     * Default value for time to live.
     */
    public static final long DEFAULT_TIME_TO_LIVE = 900L;

    /**
     * Default time to live unit.
     */
    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;

    /**
     * Default value for following redirects.
     */
    public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;

    /**
     * Default value for connection timeout.
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

    /**
     * Default value for connection timer repeat.
     */
    public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 3000;

    private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;

    private int maxConnections = DEFAULT_MAX_CONNECTIONS;

    private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    private long timeToLive = DEFAULT_TIME_TO_LIVE;

    private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;

    private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;

    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;

    public int getConnectionTimerRepeat() {
        return this.connectionTimerRepeat;
    }

    public void setConnectionTimerRepeat(int connectionTimerRepeat) {
        this.connectionTimerRepeat = connectionTimerRepeat;
    }

    public boolean isDisableSslValidation() {
        return this.disableSslValidation;
    }

    public void setDisableSslValidation(boolean disableSslValidation) {
        this.disableSslValidation = disableSslValidation;
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return this.maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public long getTimeToLive() {
        return this.timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public TimeUnit getTimeToLiveUnit() {
        return this.timeToLiveUnit;
    }

    public void setTimeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
    }

    public boolean isFollowRedirects() {
        return this.followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
