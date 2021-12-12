package com.iwbfly.myhttp.http;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 14:38
 **/
public class MyhttpHeader {
    private final String name;

    private String value;

    public MyhttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
