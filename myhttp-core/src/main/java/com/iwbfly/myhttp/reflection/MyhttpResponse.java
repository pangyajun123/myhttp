package com.iwbfly.myhttp.reflection;

import com.iwbfly.myhttp.http.ContentType;
import com.iwbfly.myhttp.http.HttpStatus;
import com.iwbfly.myhttp.http.MyhttpHeader;
import com.iwbfly.myhttp.http.MyhttpHeaderMap;

import java.io.InputStream;
import java.util.List;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 12:39
 **/
public abstract class MyhttpResponse<T> {
    protected MyhttpRequest request;
    protected volatile Integer statusCode;
    protected volatile String content;
    protected volatile String filename;
    protected volatile ContentType contentType;
    protected volatile String contentEncoding;
    protected volatile long contentLength;
    protected volatile MyhttpHeaderMap headers = new MyhttpHeaderMap();
    protected volatile T result;

    public MyhttpResponse(MyhttpRequest request) {
        this.request = request;
    }

    public MyhttpRequest getRequest() {
        return request;
    }

    public synchronized void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public MyhttpResponse<T> setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public long getContentLength() {
        return contentLength;
    }

    public boolean isSuccess() {
        return getStatusCode() >= HttpStatus.OK && getStatusCode() < HttpStatus.MULTIPLE_CHOICES;
    }


    public boolean isError() {
        return !isSuccess();
    }

    public abstract boolean isReceivedResponseData();

    public abstract byte[] getByteArray() throws Exception;

    public abstract InputStream getInputStream() throws Exception;

    public MyhttpHeader getHeader(String name) {
        return headers.getHeader(name);
    }

    public List<MyhttpHeader> getHeaders(String name) {
        return headers.getHeaders(name);
    }

    public String getHeaderValue(String name) {
        return headers.getValue(name);
    }

    public List<String> getHeaderValues(String name) {
        return headers.getValues(name);
    }

    public MyhttpHeaderMap getHeaders() {
        return headers;
    }
}
