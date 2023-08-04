package com.xebisco.yield.script.interpreter.instruction;

import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.value.ObjectValue;

import java.util.Map;

public class GetVariable implements IInstruction {

    private final Declaration declaration;

    public GetVariable(Declaration declaration) {
        this.declaration = declaration;
    }

    @Override
    public Object run(Map<Declaration, ObjectValue> declarations) {
        return declarations.get(declaration);
    }
}
