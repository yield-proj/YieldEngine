package com.xebisco.yield.script.interpreter.instruction;

import com.xebisco.yield.script.interpreter.Declaration;
import com.xebisco.yield.script.interpreter.ValueAlreadyExistsRuntimeException;
import com.xebisco.yield.script.interpreter.value.IValueGetProcess;
import com.xebisco.yield.script.interpreter.value.IValueSetProcess;
import com.xebisco.yield.script.interpreter.value.ObjectValue;

import java.util.HashMap;
import java.util.Map;

public class VariableDeclaration implements IInstruction {
    private final String variableName;
    private final IInstruction variableValue;

    private final Class<?> cast;

    private final IValueGetProcess valueGetProcess;
    private final IValueSetProcess valueSetProcess;

    public VariableDeclaration(String variableName, IInstruction variableValue, Class<?> cast, IValueGetProcess valueGetProcess, IValueSetProcess valueSetProcess) {
        this.variableName = variableName;
        this.variableValue = variableValue;
        this.cast = cast;
        this.valueGetProcess = valueGetProcess;
        this.valueSetProcess = valueSetProcess;
    }

    @Override
    public Object run(Map<Declaration, ObjectValue> declarations) {
        final var declaration = new Declaration(variableName, null);
        if(declarations.containsKey(declaration))
            throw new ValueAlreadyExistsRuntimeException(variableName);
        final var value = new ObjectValue(variableValue.run(new HashMap<>(declarations)), cast, valueGetProcess, valueSetProcess);
        declarations.put(declaration, value);
        return value.value();
    }
}