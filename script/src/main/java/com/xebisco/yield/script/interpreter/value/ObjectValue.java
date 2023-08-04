package com.xebisco.yield.script.interpreter.value;

public class ObjectValue {
    private Object value;
    private final Class<?> cast;

    private final IValueGetProcess valueGetProcess;
    private final IValueSetProcess valueSetProcess;

    public ObjectValue(Object value, Class<?> cast, IValueGetProcess valueGetProcess, IValueSetProcess valueSetProcess) {
        this.value = value;
        this.cast = cast;
        this.valueGetProcess = valueGetProcess;
        this.valueSetProcess = valueSetProcess;
    }

    public Object value() {
        return valueGetProcess.process(value);
    }

    public void setValue(Object value) {
        this.value = valueSetProcess.process(value);
    }

    public Class<?> cast() {
        return cast;
    }

    public IValueGetProcess valueGetProcess() {
        return valueGetProcess;
    }

    public IValueSetProcess valueSetProcess() {
        return valueSetProcess;
    }
}
