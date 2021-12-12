package com.iwbfly.myhttp.converter.text;

import com.iwbfly.myhttp.converter.Decoder;

import java.lang.reflect.Type;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 23:29
 **/
public class DefaultTextConverter implements Decoder<String> {
    @Override
    public <T> T decode(String source, Class<T> targetClass) {
        return (T) source;
    }

    @Override
    public <T> T decode(String source, Type targetClass) {
        return (T) source;
    }
}
