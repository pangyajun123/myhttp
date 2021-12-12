package com.iwbfly.myhttp;

import com.iwbfly.myhttp.http.Request;
import com.iwbfly.myhttp.http.Request.*;
import com.iwbfly.myhttp.http.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 21:30
 **/
public interface Client {

    Response execute(Request request, Options options) throws IOException;
    Response asynExecute(Request request, Options options) throws IOException;

    class Default implements Client {

        private final SSLSocketFactory sslContextFactory;
        private final HostnameVerifier hostnameVerifier;

        /**
         * Null parameters imply platform defaults.
         */
        public Default(SSLSocketFactory sslContextFactory, HostnameVerifier hostnameVerifier) {
            this.sslContextFactory = sslContextFactory;
            this.hostnameVerifier = hostnameVerifier;
        }

        @Override
        public Response execute(Request request, Options options) throws IOException {
            HttpURLConnection connection = convertAndSend(request, options);
            return convertResponse(connection, request);
        }

        @Override
        public Response asynExecute(Request request, Options options) throws IOException {
            return Response.builder().build();
        }

        HttpURLConnection convertAndSend(Request request, Options options) throws IOException {
            final HttpURLConnection connection =
                    (HttpURLConnection) new URL(request.url()).openConnection();
            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection sslCon = (HttpsURLConnection) connection;
                if (sslContextFactory != null) {
                    sslCon.setSSLSocketFactory(sslContextFactory);
                }
                if (hostnameVerifier != null) {
                    sslCon.setHostnameVerifier(hostnameVerifier);
                }
            }
            connection.setConnectTimeout(options.connectTimeoutMillis());
            connection.setReadTimeout(options.readTimeoutMillis());
            connection.setAllowUserInteraction(false);
            connection.setInstanceFollowRedirects(options.isFollowRedirects());
            connection.setRequestMethod(request.httpMethod().name());


            boolean hasAcceptHeader = false;
            Integer contentLength = null;
            for (String field : request.headers().keySet()) {
                if (field.equalsIgnoreCase("Accept")) {
                    hasAcceptHeader = true;
                }
                for (String value : request.headers().get(field)) {
                    connection.addRequestProperty(field, value);
                }
            }
            // Some servers choke on the default accept string.
            if (!hasAcceptHeader) {
                connection.addRequestProperty("Accept", "*/*");
            }

            if (request.requestBody().asBytes() != null) {
                if (contentLength != null) {
                    connection.setFixedLengthStreamingMode(contentLength);
                } else {
                    connection.setChunkedStreamingMode(8196);
                }
                connection.setDoOutput(true);
                OutputStream out = connection.getOutputStream();
                try {
                    out.write(request.requestBody().asBytes());
                } finally {
                    try {
                        out.close();
                    } catch (IOException suppressed) { // NOPMD
                    }
                }
            }
            return connection;
        }

        Response convertResponse(HttpURLConnection connection, Request request) throws IOException {
            int status = connection.getResponseCode();
            String reason = connection.getResponseMessage();

            if (status < 0) {
                throw new IOException(format("Invalid status(%s) executing %s %s", status,
                        connection.getRequestMethod(), connection.getURL()));
            }

            Map<String, Collection<String>> headers = new LinkedHashMap<String, Collection<String>>();
            for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
                // response message
                if (field.getKey() != null) {
                    headers.put(field.getKey(), field.getValue());
                }
            }

            Integer length = connection.getContentLength();
            if (length == -1) {
                length = null;
            }
            InputStream stream;
            if (status >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }
            return Response.builder()
                    .status(status)
                    .reason(reason)
                    .headers(headers)
                    .request(request)
                    .body(stream, length)
                    .build();
        }
    }
}
