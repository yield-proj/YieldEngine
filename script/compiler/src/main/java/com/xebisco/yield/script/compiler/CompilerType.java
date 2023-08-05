package com.xebisco.yield.script.compiler;

import com.xebisco.yield.script.interpreter.instruction.IInstruction;

import java.util.Objects;

public record CompilerType(String text, Class<?> type, boolean isImmutable, boolean isFunction, IInstruction instruction) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompilerType that = (CompilerType) o;
        if(that.text == null || text == null) return false;
        return isFunction == that.isFunction && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, isFunction);
    }
}
