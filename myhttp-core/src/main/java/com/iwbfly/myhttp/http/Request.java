package com.iwbfly.myhttp.http;

import com.iwbfly.myhttp.utils.Util;

import java.nio.charset.Charset;
import java.util.*;


/**
 * @auther: pangyajun
 * @create: 2021/11/23 09:52
 **/
public class Request {

    public static class Body {

        private final byte[] data;
        private final Charset encoding;

        private Body(byte[] data, Charset encoding) {
            super();
            this.data = data;
            this.encoding = encoding;

        }

        public static Request.Body encoded(byte[] bodyData, Charset encoding) {
            return new Request.Body(bodyData, encoding);
        }

        public int length() {
            /* calculate the content length based on the data provided */
            return data != null ? data.length : 0;
        }

        public byte[] asBytes() {
            return data;
        }

        public String asString() {
            return encoding != null && data != null
                    ? new String(data, encoding)
                    : "Binary data";
        }

        public static Body empty() {
            return new Request.Body(null, null);
        }

    }
    private final HttpMethod httpMethod;
    private final String url;
    private final Map<String, Collection<String>> headers;
    private final Body body;

    public static Request create(String method,
                                 String url,
                                 Map<String, Collection<String>> headers,
                                 byte[] body,
                                 Charset charset) {
        Util.checkNotNull(method, "httpMethod of %s", method);
        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        return create(httpMethod, url, headers, body, charset);
    }

    Request(HttpMethod method, String url, Map<String, Collection<String>> headers, Body body) {
        this.httpMethod = Util.checkNotNull(method, "httpMethod of %s", method.name());
        this.url = Util.checkNotNull(url, "url");
        this.headers = Util.checkNotNull(headers, "headers of %s %s", method, url);
        this.body = body;
    }

    public HttpMethod httpMethod() {
        return this.httpMethod;
    }

    public String url() {
        return url;
    }

    public Map<String, Collection<String>> headers() {
        return headers;
    }

    public Body requestBody() {
        return body;
    }

    public Charset charset() {
        return body.encoding;
    }
    public static Request create(HttpMethod httpMethod,
                                 String url,
                                 Map<String, Collection<String>> headers,
                                 byte[] body,
                                 Charset charset) {
        return create(httpMethod, url, headers, Body.encoded(body, charset));
    }

    public static Request create(HttpMethod httpMethod,
                                 String url,
                                 Map<String, Collection<String>> headers,
                                 Body body) {
        return new Request(httpMethod, url, headers, body);
    }
    public enum HttpMethod {
        GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH
    }
    public static class Options {

        private final int connectTimeoutMillis;
        private final int readTimeoutMillis;
        private final boolean followRedirects;

        public Options(int connectTimeoutMillis, int readTimeoutMillis, boolean followRedirects) {
            this.connectTimeoutMillis = connectTimeoutMillis;
            this.readTimeoutMillis = readTimeoutMillis;
            this.followRedirects = followRedirects;
        }

        public Options(int connectTimeoutMillis, int readTimeoutMillis) {
            this(connectTimeoutMillis, readTimeoutMillis, true);
        }
        public Options( int readTimeoutMillis) {
            this(3*1000, readTimeoutMillis);
        }
        public Options() {
            this(3 * 1000, 10 * 1000);
        }

        public int connectTimeoutMillis() {
            return connectTimeoutMillis;
        }

        public int readTimeoutMillis() {
            return readTimeoutMillis;
        }

        public boolean isFollowRedirects() {
            return followRedirects;
        }
    }
}
