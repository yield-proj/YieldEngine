package com.xebisco.yield.script.compiler;

import java.util.ArrayList;
import java.util.List;

public class CompilerBank {
    private final List<CompilerType> compilerTypes = new ArrayList<>();

    public CompilerBank() {
    }

    public CompilerBank(CompilerBank parent) {
        this.compilerTypes.addAll(parent.compilerTypes);
    }

    public List<CompilerType> compilerTypes() {
        return compilerTypes;
    }
}
