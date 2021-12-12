package com.iwbfly.myhttp.logging;

import com.iwbfly.myhttp.http.Request;
import com.iwbfly.myhttp.http.Response;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @auther: pangyajun
 * @create: 2021/12/1 15:30
 **/
public class Slf4jLogger extends Logger{

    private final org.slf4j.Logger logger;

    public Slf4jLogger() {
        this(Logger.class);
    }

    public Slf4jLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public Slf4jLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }
    @Override
    public void logRequest(Request request, MyhttpRequest myhttpRequest) {
        if (logger.isInfoEnabled()) {
            try {
                super.logRequest(request, myhttpRequest);
            } finally {

            }
        }
    }
    @Override
    public Response logAndRebufferResponse(Response response, long elapsedTime,MyhttpRequest myhttpRequest)
            throws IOException {
        if (logger.isInfoEnabled()) {
            try {
                return super.logAndRebufferResponse(response, elapsedTime, myhttpRequest);
            } finally {

            }

        }
        return response;
    }
    @Override
    public void log(String format,Object... args) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format(format, args));
        }
    }
}
