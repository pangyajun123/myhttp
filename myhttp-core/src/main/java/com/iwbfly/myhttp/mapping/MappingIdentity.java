package com.iwbfly.myhttp.mapping;

public class MappingIdentity extends MappingExpr {

    private String name;

    public MappingIdentity(String name) {
        super(Token.ID);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object render(Object[] args) {
        return name;
    }

    @Override
    public String toString() {
        return "[ID: " + name + "]";
    }
}
