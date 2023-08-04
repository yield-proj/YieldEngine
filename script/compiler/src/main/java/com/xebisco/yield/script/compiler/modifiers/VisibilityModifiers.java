package com.xebisco.yield.script.compiler.modifiers;

public enum VisibilityModifiers {
    PUBLIC("public"), PRIVATE("private");


    private final String text;

    VisibilityModifiers(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }

    public static VisibilityModifiers getMod(String s) throws UnknownModifierException {
        for(VisibilityModifiers mod : VisibilityModifiers.values()) {
            if(mod.text.equals(s)) return mod;
        }
        throw new UnknownModifierException(s);
    }
}
