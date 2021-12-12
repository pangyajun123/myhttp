package com.iwbfly.myhttp.http;

import com.iwbfly.myhttp.Client;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 21:32
 **/
public final class OkHttpClient implements Client {
    private final okhttp3.OkHttpClient delegate;

    public OkHttpClient() {
        this(new okhttp3.OkHttpClient());
    }

    public OkHttpClient(okhttp3.OkHttpClient delegate) {
        this.delegate = delegate;
    }

    static okhttp3.Request toOkHttpRequest(Request input) {
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(input.url());
        MediaType mediaType = null;
        boolean hasAcceptHeader = false;
        Iterator var4 = input.headers().keySet().iterator();

        while(var4.hasNext()) {
            String field = (String)var4.next();
            if (field.equalsIgnoreCase("Accept")) {
                hasAcceptHeader = true;
            }

            Iterator var6 = ((Collection)input.headers().get(field)).iterator();

            while(var6.hasNext()) {
                String value = (String)var6.next();
                requestBuilder.addHeader(field, value);
                if (field.equalsIgnoreCase("Content-Type")) {
                    mediaType = MediaType.parse(value);
                    if (input.charset() != null) {
                        mediaType.charset(input.charset());
                    }
                }
            }
        }

        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }

        byte[] inputBody = input.requestBody().asBytes();
        boolean isMethodWithBody = Request.HttpMethod.POST == input.httpMethod() || Request.HttpMethod.PUT == input.httpMethod() || Request.HttpMethod.PATCH == input.httpMethod();
        if (isMethodWithBody) {
            requestBuilder.removeHeader("Content-Type");
            if (inputBody == null) {
                inputBody = new byte[0];
            }
        }

        RequestBody body = inputBody != null ? RequestBody.create(mediaType, inputBody) : null;
        requestBuilder.method(input.httpMethod().name(), body);
        return requestBuilder.build();
    }

    private static Response toMyhttpResponse(okhttp3.Response response, Request request) throws IOException {
        return Response.builder().status(response.code()).reason(response.message()).request(request).headers(toMap(response.headers())).body(toBody(response.body())).build();
    }

    private static Map toMap(Headers headers) {
        return headers.toMultimap();
    }

    private static Response.Body toBody(final ResponseBody input) throws IOException {
        if (input != null && input.contentLength() != 0L) {
            final Integer length = input.contentLength() >= 0L && input.contentLength() <= 2147483647L ? (int)input.contentLength() : null;
            return new Response.Body() {
                public void close() throws IOException {
                    input.close();
                }

                public Integer length() {
                    return length;
                }

                public boolean isRepeatable() {
                    return false;
                }

                public InputStream asInputStream() throws IOException {
                    return input.byteStream();
                }

                public Reader asReader() throws IOException {
                    return input.charStream();
                }

                public Reader asReader(Charset charset) throws IOException {
                    return this.asReader();
                }

                public String string() throws IOException {
                    return input.string();
                }
            };
        } else {
            if (input != null) {
                input.close();
            }

            return null;
        }
    }

    public Response execute(Request input, Request.Options options) throws IOException {
        okhttp3.OkHttpClient requestScoped;
        if (this.delegate.connectTimeoutMillis() == options.connectTimeoutMillis() && this.delegate.readTimeoutMillis() == options.readTimeoutMillis() && this.delegate.followRedirects() == options.isFollowRedirects()) {
            requestScoped = this.delegate;
        } else {
            requestScoped = this.delegate.newBuilder().connectTimeout((long)options.connectTimeoutMillis(), TimeUnit.MILLISECONDS).readTimeout((long)options.readTimeoutMillis(), TimeUnit.MILLISECONDS).followRedirects(options.isFollowRedirects()).build();
        }

        okhttp3.Request request = toOkHttpRequest(input);
        okhttp3.Response response = requestScoped.newCall(request).execute();
        return toMyhttpResponse(response, input).toBuilder().request(input).build();
    }

    @Override
    public Response asynExecute(Request request, Request.Options options) throws IOException {
        return Response.builder().build();
    }
}
