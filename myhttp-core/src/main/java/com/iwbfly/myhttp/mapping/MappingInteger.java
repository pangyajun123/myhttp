package com.iwbfly.myhttp.mapping;


public class MappingInteger extends MappingExpr {

    private final int number;

    public MappingInteger(int number) {
        super(Token.INT);
        this.number = number;
    }

    @Override
    public Object render(Object[] args) {
        return number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "[Int: " + number + "]";
    }
}
