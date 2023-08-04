package com.xebisco.yield.script.compiler;

import com.xebisco.yield.script.interpreter.instruction.IInstruction;

import java.util.Objects;

public record CompilerType(String name, Class<?> type, boolean isImmutable, boolean isFunction, IInstruction instruction) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilerType that = (CompilerType) o;
        if(name == null || that.name == null) return false;
        return isFunction == that.isFunction && Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }
}
