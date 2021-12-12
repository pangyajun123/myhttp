package com.iwbfly.myhttp.logging;

import com.iwbfly.myhttp.http.Request;
import com.iwbfly.myhttp.http.Response;
import com.iwbfly.myhttp.reflection.MyhttpRequest;
import com.iwbfly.myhttp.utils.Util;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

import static com.iwbfly.myhttp.utils.Util.valuesOrEmpty;

/**
 * @auther: pangyajun
 * @create: 2021/12/1 15:24
 **/
public abstract class Logger {

    public abstract void log(String formart, Object... args);

    public void logRequest(Request request, MyhttpRequest myhttpRequest) {
        Logger.Level loggerLevel = myhttpRequest.getLoggerLevel();
        boolean headerLog = false;
        boolean bodyLog = false;
        log("---> %s %s HTTP/1.1", request.httpMethod().name(), request.url());
        Message message = new Message();
        Map<String, Collection<String>> logHeaders = null;
        if (request.headers().size() > 0 && loggerLevel.ordinal() > Level.BASIC.ordinal()) {
            headerLog = true;
            logHeaders = clone(request.headers());
            message.setVariable(logHeaders);
        }
        String bodyText = "";
        int bodyLength = 0;
        if (request.requestBody().asBytes() != null) {
            bodyLog = true;
            bodyLength = request.requestBody().asBytes().length;
            bodyText = request.charset() != null
                    ? new String(request.requestBody().asBytes(), request.charset())
                    : null;
            message.setMessage(bodyText);
        }
        myhttpRequest.getInterceptorChain().requestLog(message);
        bodyText = message.getMessage();
        if (headerLog && logHeaders != null && logHeaders.size() > 0) {
            log("---> HEADER START");
            for (String field : logHeaders.keySet()) {
                for (String value : valuesOrEmpty(logHeaders, field)) {
                    log("%s: %s", field, value);
                }
            }
            log("---> HEADER END");
        }
        if (bodyLog) {
            log("---> END HTTP BODY (%s)", bodyText);
        }
        log("---> END HTTP (%s-byte body)", bodyLength);

    }

    public Response logAndRebufferResponse(Response response, long elapsedTime, MyhttpRequest myhttpRequest) throws IOException {
        Logger.Level loggerLevel = myhttpRequest.getLoggerLevel();
        String reason = response.reason() != null ? " " + response.reason() : "";
        int status = response.status();
        boolean headerLog = false;
        boolean bodyLog = false;
        Message message = new Message();
        log("<--- HTTP/1.1 %s%s (%sms)", status, reason, elapsedTime);
        Map<String, Collection<String>> logHeaders = null;
        if (response.headers().size() > 0 && loggerLevel.ordinal() > Level.BASIC.ordinal()) {
            headerLog = true;
            logHeaders = clone(response.headers());
            message.setVariable(logHeaders);
        }
        int bodyLength = 0;
        String bodyText = "";
        byte[] bodyData = null;
        if (response.body() != null && !(status == 204 || status == 205)) {
            bodyLog = true;
            bodyData = Util.toByteArray(response.body().asInputStream());
            bodyLength = bodyData.length;
            bodyText = response.request().charset() != null
                    ? new String(bodyData, response.request().charset())
                    : null;
            message.setMessage(bodyText);
        }
        myhttpRequest.getInterceptorChain().responseLog(message);
        bodyText = message.getMessage();
        if (headerLog && logHeaders != null && logHeaders.size() > 0) {
            log("<--- HEADER START");
            for (String field : logHeaders.keySet()) {
                for (String value : valuesOrEmpty(logHeaders, field)) {
                    log("%s: %s", field, value);
                }
            }
            log("<--- HEADER END");
        }
        if (bodyLog) {
            log("<--- END HTTP BODY (%s)", bodyText);
            log("<--- END HTTP (%s-byte body)", bodyLength);
            return response.toBuilder().body(bodyData).build();
        } else {
            log("<--- END HTTP (%s-byte body)", bodyLength);
        }
        return response;
    }

    Map<String, Collection<String>> clone(Map<String, Collection<String>> headers) {
        Map<String, Collection<String>> logHeaders = new HashMap<>();
        for (String field : headers.keySet()) {
            List list = new ArrayList();
            logHeaders.put(field, list);
            for (String value : valuesOrEmpty(headers, field)) {
                list.add(value);
            }
        }
        return logHeaders;
    }

    public IOException logIOException(IOException ioe, long elapsedTime) {
        log("<--- ERROR %s: %s (%sms)", ioe.getClass().getSimpleName(),
                ioe.getMessage(),
                elapsedTime);
        StringWriter sw = new StringWriter();
        ioe.printStackTrace(new PrintWriter(sw));
        log("%s", sw.toString());
        log("<--- END ERROR");
        return ioe;
    }

    public enum Level {
        /**
         * No logging.
         */
        NONE,
        /**
         * Log only the request method and URL and the response status code and execution time.
         */
        BASIC,
        /**
         * Log the headers, body, and metadata for both requests and responses.
         */
        FULL
    }
}
