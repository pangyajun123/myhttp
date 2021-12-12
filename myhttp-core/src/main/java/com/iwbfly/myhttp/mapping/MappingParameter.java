package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.filter.Filter;
import com.iwbfly.myhttp.filter.FilterChain;

public class MappingParameter {

    public final static int TARGET_UNKNOWN = 0;
    public final static int TARGET_QUERY = 1;
    public final static int TARGET_BODY = 2;
    public final static int TARGET_HEADER = 3;

    protected final Class type;

    protected Integer index;

    protected String name;

    protected int target = TARGET_UNKNOWN;

    private boolean objectProperties = false;

    private boolean isJsonParam = false;

    private String jsonParamName;

    private FilterChain filterChain = new FilterChain();

    public MappingParameter(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUnknownTarget() {
        return target == TARGET_UNKNOWN;
    }

    public boolean isQuery() {
        return target == TARGET_QUERY;
    }

    public boolean isBody() {
        return target == TARGET_BODY;
    }

    public boolean isHeader() {
        return target == TARGET_HEADER;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public boolean isObjectProperties() {
        return objectProperties;
    }

    public void setObjectProperties(boolean objectProperties) {
        this.objectProperties = objectProperties;
    }

    public boolean isJsonParam() {
        return isJsonParam;
    }

    public void setJsonParam(boolean jsonParam) {
        isJsonParam = jsonParam;
    }

    public String getJsonParamName() {
        return jsonParamName;
    }

    public void setJsonParamName(String jsonParamName) {
        this.jsonParamName = jsonParamName;
    }

    public FilterChain getFilterChain() {
        return filterChain;
    }

    public void addFilter(Filter filter) {
        filterChain.addFilter(filter);
    }
}
