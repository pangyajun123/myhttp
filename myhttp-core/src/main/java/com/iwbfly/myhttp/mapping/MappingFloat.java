package com.iwbfly.myhttp.mapping;

public class MappingFloat extends MappingExpr {

    private final float number;

    public MappingFloat(float number) {
        super(Token.FLOAT);
        this.number = number;
    }

    @Override
    public Object render(Object[] args) {
        return number;
    }

    public float getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "[Float: " + number + "]";
    }

}
