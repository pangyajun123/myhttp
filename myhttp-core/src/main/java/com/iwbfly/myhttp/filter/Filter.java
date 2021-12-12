package com.iwbfly.myhttp.filter;

import com.iwbfly.myhttp.Myhttp;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 11:10
 **/
public interface Filter {
    Object doFilter(Myhttp.Builder configuration, Object data);
}
