package com.iwbfly.myhttp.filter;

import com.iwbfly.myhttp.Myhttp;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 11:48
 **/
public class FilterChain implements Filter {

    private LinkedList<Filter> filters = new LinkedList<>();

    @Override
    public Object doFilter(Myhttp.Builder configuration, Object data) {
        Iterator<Filter> iter = filters.iterator();
        Object result = data;
        for ( ; iter.hasNext(); ) {
            Filter filter = iter.next();
            result = filter.doFilter(configuration, result);
        }
        return result;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public boolean isEmpty() {
        return filters.isEmpty();
    }
}

