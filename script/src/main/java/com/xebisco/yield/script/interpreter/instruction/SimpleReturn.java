package com.xebisco.yield.script.interpreter.instruction;

import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.value.ObjectValue;

import java.util.Map;

public class SimpleReturn implements IInstruction {

    private final Object value;

    public SimpleReturn(Object value) {
        this.value = value;
    }

    @Override
    public Object run(Map<Declaration, ObjectValue> declarations) {
        return value;
    }
}
