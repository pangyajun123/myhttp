package com.iwbfly.myhttp.mapping;


public class MappingDouble extends MappingExpr {

    private final double number;

    public MappingDouble(double number) {
        super(Token.DOUBLE);
        this.number = number;
    }

    @Override
    public Object render(Object[] args) {
        return number;
    }

    public double getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "[Double: " + number + "]";
    }

}
