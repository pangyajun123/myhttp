package com.iwbfly.myhttp.converter.json;

import com.iwbfly.myhttp.converter.Decoder;
import com.iwbfly.myhttp.converter.Encoder;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 13:37
 **/
public interface JsonConverter extends Encoder, Decoder<String> {

    Map<String, Object> convertObjectToMap(Object obj);

}
