package com.iwbfly.myhttp.reflection;

/**
 * @auther: pangyajun
 * @create: 2021/11/23 14:43
 **/
public interface MyhttpMethodHandler {

    Object invoke(Object[] argv) throws Throwable;

}
