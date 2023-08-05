package com.xebisco.yield.script.interpreter.value;

public class ValueImmutable implements IValueSetProcess {
    @Override
    public Object process(Object o) {
        throw new ImmutableBreakRuntimeException();
    }
}
