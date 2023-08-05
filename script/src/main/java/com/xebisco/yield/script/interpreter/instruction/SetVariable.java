package com.xebisco.yield.script.interpreter.instruction;

import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.value.ObjectValue;
import com.xebisco.yield.script.interpreter.value.ValueDoesntExistsRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class SetVariable implements IInstruction {
    private final String variableName;
    private final IInstruction variableValue;

    public SetVariable(String variableName, IInstruction variableValue) {
        this.variableName = variableName;
        this.variableValue = variableValue;
    }

    @Override
    public Object run(Map<Declaration, ObjectValue> declarations) {
        final var declaration = new Declaration(variableName, null);
        if(!declarations.containsKey(declaration))
            throw new ValueDoesntExistsRuntimeException(variableName);
        return declarations.get(declaration).setValue(variableValue.run(new HashMap<>(declarations))).value();
    }
}
