package com.iwbfly.myhttp.converter;

import java.lang.reflect.Type;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 13:32
 **/
public interface Decoder<S> {

    <T> T decode(S source, Class<T> targetClass);
    <T> T decode(S source, Type targetClass);

}
