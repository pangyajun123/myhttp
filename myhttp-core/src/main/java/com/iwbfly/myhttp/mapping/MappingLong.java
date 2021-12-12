package com.iwbfly.myhttp.mapping;

public class MappingLong extends MappingExpr {

    private final long number;

    public MappingLong(long number) {
        super(Token.LONG);
        this.number = number;
    }

    @Override
    public Object render(Object[] args) {
        return number;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "[Long: " + number + "]";
    }

}
