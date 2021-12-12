package com.iwbfly.myhttp.http;

import java.util.*;

/**
 * @auther: pangyajun
 * @create: 2021/11/27 14:39
 **/
public class MyhttpHeaderMap {

    private final List<MyhttpHeader> headers;

    public MyhttpHeaderMap(List<MyhttpHeader> headers) {
        this.headers = headers;
    }

    public MyhttpHeaderMap() {
        this.headers = new LinkedList<>();
    }


    public int size() {
        return headers.size();
    }

    public String getValue(String name) {
        MyhttpHeader header = getHeader(name);
        if (header != null) {
            return header.getValue();
        }
        return null;
    }

    public List<String> getValues(String name) {
        List<String> results = new ArrayList<>(2);
        for (MyhttpHeader header : headers) {
            if (header.getName().equalsIgnoreCase(name)) {
                results.add(header.getValue());
            }
        }
        return Collections.unmodifiableList(results);
    }


    public MyhttpHeader getHeader(String name) {
        for (MyhttpHeader header : headers) {
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
        }
        return null;
    }

    public List<MyhttpHeader> getHeaders(String name) {
        List<MyhttpHeader> results = new ArrayList<>(2);
        for (MyhttpHeader header : headers) {
            if (header.getName().equalsIgnoreCase(name)) {
                results.add(header);
            }
        }
        return results;
    }


    public List<String> names() {
        List<String> results = new ArrayList<>(headers.size());
        for (MyhttpHeader header : headers) {
            results.add(header.getName());
        }
        return Collections.unmodifiableList(results);
    }


    public List<String> getValues() {
        List<String> results = new ArrayList<>(headers.size());
        for (MyhttpHeader header : headers) {
            results.add(header.getValue());
        }
        return Collections.unmodifiableList(results);
    }

    public void addHeader(MyhttpHeader header) {
        headers.add(header);
    }

    public void addHeader(String name, String value) {
        addHeader(new MyhttpHeader(name, value));
    }

    public void setHeader(String name, String value) {
        MyhttpHeader header = getHeader(name);
        if (header != null) {
            header.setValue(value);
        } else {
            addHeader(name, value);
        }
    }


    public Iterator<MyhttpHeader> headerIterator() {
        return headers.iterator();
    }

    public void remove(String name) {
        MyhttpHeader header = getHeader(name);
        if (header != null) {
            headers.remove(header);
        }
    }
}
