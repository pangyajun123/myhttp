package com.iwbfly.myhttp.http;

import com.iwbfly.myhttp.utils.StringUtils;

import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_BODY;
import static com.iwbfly.myhttp.mapping.MappingParameter.TARGET_QUERY;

public enum MyhttpRequestType {

    GET("GET", TARGET_QUERY),
    POST("POST", TARGET_BODY),
    PUT("PUT", TARGET_BODY),
    PATCH("PATCH", TARGET_BODY),
    HEAD("HEAD", TARGET_QUERY),
    OPTIONS("OPTIONS", TARGET_QUERY),
    DELETE("DELETE", TARGET_QUERY),
    TRACE("TRACE", TARGET_QUERY),
    ;

    private final String name;
    private final int defaultParamTarget;

    MyhttpRequestType(String name, int defaultParamTarget) {
        this.name = name;
        this.defaultParamTarget = defaultParamTarget;
    }

    public String getName() {
        return name;
    }

    public int getDefaultParamTarget() {
        return defaultParamTarget;
    }

    public boolean match(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return this.name.equals(name.toUpperCase());
    }

    public static MyhttpRequestType findType(String name) {
        for (MyhttpRequestType type : MyhttpRequestType.values()) {
            if (type.match(name)) {
                return type;
            }
        }
        return null;
    }
}
