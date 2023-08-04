package com.xebisco.yield.script.compiler.modifiers;

public enum VariableModifiers {
    IMMUTABLE("let"), MUTABLE("var");

    private final String text;

    VariableModifiers(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static VariableModifiers getMod(String s) throws UnknownModifierException {
        for(VariableModifiers mod : VariableModifiers.values()) {
            if(mod.text.equals(s)) return mod;
        }
        throw new UnknownModifierException(s);
    }
}
