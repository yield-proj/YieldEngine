package com.xebisco.yasm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Program extends Mem implements Serializable {
    private final Map<String, Integer> regs;
    private final Properties properties = new Properties();
    private final Map<String, Instruction[]> scopes = new HashMap<>();

    public Program() {
        //mt = Math Target; ccl = Calls Control; sdbt = String Database Target
        regs = Map.of("a", 0, "b", 1, "c", 2, "d", 3, "e", 4, "f", 5, "g", 6, "mt", 7, "ccl", 8, "sdbt", 9);
    }

    public final Instruction[] global() {
        try {
            return scopes.get(properties.getProperty("global"));
        } catch (NullPointerException e) {
            throw new IllegalStateException(e);
        }
    }

    public Map<String, Integer> regs() {
        return regs;
    }

    public Properties properties() {
        return properties;
    }

    public Map<String, Instruction[]> scopes() {
        return scopes;
    }
}
