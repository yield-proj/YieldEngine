package com.xebisco.yield.script.compiler.modifiers;

public enum GlobalScopeModifiers {
    STATIC("static");

    private final String text;

    GlobalScopeModifiers(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static GlobalScopeModifiers getMod(String s) throws UnknownModifierException {
        for(GlobalScopeModifiers mod : GlobalScopeModifiers.values()) {
            if(mod.text.equals(s)) return mod;
        }
        throw new UnknownModifierException(s);
    }
}
