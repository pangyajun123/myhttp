package com.iwbfly.mock;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Assert;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @auther: pangyajun
 * @create: 2021/12/4 14:53
 **/
public class MockServerRequest {
    private final RecordedRequest request;

    private final HttpUrl url;

    private String stringBodyValue;


    public MockServerRequest(RecordedRequest request) {
        this.request = request;
        this.url = request.getRequestUrl();
    }

    public static MockServerRequest mockRequest(MockWebServer server) {
        try {
            RecordedRequest request = server.takeRequest();
            return new MockServerRequest(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String requestLine() {
        return request.getRequestLine();
    }

    public String scheme() {
        return url.scheme();
    }

    public String method() {
        return request.getMethod();
    }

    public String path() {
        return url.encodedPath();
    }

    public String query() {
        return url.query();
    }

    public String encodedQuery() {
        return url.encodedQuery();
    }

    public String query(String name) {
        return url.queryParameter(name);
    }

    public Headers headers() {
        return request.getHeaders();
    }

    public String header(String name) {
        return request.getHeader(name);
    }

    public byte[] bodyAsBytes() {
        return request.getBody().readByteArray();
    }

    public InputStream bodyAsInputStream() {
        return request.getBody().inputStream();
    }


    public String bodyAsString(Charset charset) {
        if (stringBodyValue == null) {
            stringBodyValue = request.getBody().readString(charset);
        }
        return stringBodyValue;
    }

    public String bodyAsString() {
        return bodyAsString(StandardCharsets.UTF_8);
    }

    public MockServerRequest assertRequestLineEquals(String expected) {
        Assert.assertEquals(expected, requestLine());
        return this;
    }


    public MockServerRequest assertSchemeEquals(String expected) {
        Assert.assertEquals(expected, scheme());
        return this;
    }


    public MockServerRequest assertMethodEquals(String expected) {
        Assert.assertEquals(expected, method());
        return this;
    }

    public MockServerRequest assertBodyNotEmpty() {
        Assert.assertNotEquals(0, request.getBodySize());
        return this;
    }

    public MockServerRequest assertBodyEmpty() {
        Assert.assertEquals(0, request.getBodySize());
        return this;
    }

    public MockServerRequest assertBodyEquals(String expected) {
        Assert.assertEquals(expected, bodyAsString());
        return this;
    }

    public MockServerRequest assertBodyEquals(byte[] expected) {
        Assert.assertArrayEquals(expected, bodyAsBytes());
        return this;
    }

    public MockServerRequest assertPathEquals(String path) {
        Assert.assertEquals(path, path());
        return this;
    }

    public MockServerRequest assertQueryEquals(String expected) {
        Assert.assertEquals(expected, query());
        return this;
    }

    public MockServerRequest assertQueryEquals(String name, String expected) {
        Assert.assertEquals(expected, query(name));
        return this;
    }

    public MockServerRequest assertEncodedQueryEquals(String expected) {
        Assert.assertEquals(expected, encodedQuery());
        return this;
    }


    public MockServerRequest assertHeaderEquals(String name, String expected) {
        Assert.assertEquals(expected, header(name));
        return this;
    }

}
