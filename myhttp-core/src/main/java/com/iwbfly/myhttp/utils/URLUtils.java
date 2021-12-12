package com.iwbfly.myhttp.utils;

import com.iwbfly.myhttp.HttpMethod;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.reflection.MyhttpRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_QUERY;


public final class URLUtils {

    private URLUtils() {
    }

    public static boolean hasProtocol(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static void checkBaseURL(String baseUrl) {
        if (!hasProtocol(baseUrl)) {
            throw new MyhttpRuntimeException("base url has no protocol: " + baseUrl);
        }
    }

    public static String getValidBaseURL(String baseUrl) {
        if (baseUrl.endsWith("/")) {
            int i = baseUrl.length() - 1;
            do {
                char ch = baseUrl.charAt(i);
                if (ch != '/') {
                    break;
                }
                i--;
            } while (i > 0);
            return baseUrl.substring(0, i + 1);
        }
        return baseUrl;
    }


    public static String getValidURL(String baseURL, String uri) {
        if (StringUtils.isNotEmpty(baseURL) && !URLUtils.hasProtocol(baseURL)) {
            baseURL = "http://" + baseURL;
        }

        if (!URLUtils.hasProtocol(uri)) {
            if (StringUtils.isNotEmpty(baseURL)) {
                if (StringUtils.isBlank(uri)) {
                    return baseURL;
                }
                if (baseURL.endsWith("/")) {
                    baseURL = getValidBaseURL(baseURL);
                }
                if (!uri.startsWith("/")) {
                    uri = "/" + uri;
                }
                return baseURL + uri;
            }
            else {
                return  "http://" + uri;
            }
        }
        return uri;
    }

    public static void urlHandle(MyhttpURL myhttpURL, MyhttpRequest request) throws MalformedURLException {
        List<RequestNameValue> nameValueList = new ArrayList<>();
        URL url = new URL(myhttpURL.getUrl());
        String newUrl = "";
        String protocol = url.getProtocol();
        String query = url.getQuery();
        if (StringUtils.isNotEmpty(query)) {
            String[] params = query.split("&");
            StringBuilder queryBuilder = new StringBuilder();
            if (params.length > 0) {
                queryBuilder.append("?");
            }
            for (int i = 0; i < params.length; i++) {
                String p = params[i];
                String[] nameValue = p.split("=");
                String name = nameValue[0];
                queryBuilder.append(name);
                RequestNameValue requestNameValue = new RequestNameValue(name, TARGET_QUERY);
                nameValueList.add(requestNameValue);
                if (nameValue.length > 1) {
                    String value = nameValue[1];
                    queryBuilder.append("=");
                    queryBuilder.append(value);
                    requestNameValue.setValue(value);
                }
                if (i < params.length - 1) {
                    queryBuilder.append("&");
                }
            }
        }
        request.addData(nameValueList);
        protocol = url.getProtocol();
        int port = url.getPort();
        newUrl = protocol + "://" + url.getHost();
        if (port != 80 && port > -1) {
            newUrl += ":" + port;
        }
        String path = url.getPath();
        if (StringUtils.isNotEmpty(path)) {
            newUrl += path;
        }
        myhttpURL.setUrl(newUrl).setProtocol(protocol).setPort(port);

    }
    public static class MyhttpURL{
        private String url;
        private int port;
        private String protocol;

        public MyhttpURL(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public MyhttpURL setUrl(String url) {
            this.url = url;
            return this;
        }

        public int getPort() {
            return port;
        }

        public MyhttpURL setPort(int port) {
            this.port = port;
            return this;
        }

        public String getProtocol() {
            return protocol;
        }

        public MyhttpURL setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }
    }
}
