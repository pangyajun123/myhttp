package com.iwbfly.myhttp;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE");

    private String value;

    HttpMethod(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }
}
