package com.iwbfly.myhttp.utils;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 14:51
 **/
public enum DataType {
    AUTO("auto"),

    TEXT("text"),

    JSON("json"),

    XML("xml"),

    BINARY("binary"),

            ;

    private String name;

    DataType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
