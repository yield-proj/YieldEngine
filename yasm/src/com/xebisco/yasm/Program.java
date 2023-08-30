package com.xebisco.yasm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Map.entry;

public class Program extends Mem implements Serializable {
    private final Map<String, Integer> regs;
    private final Properties properties = new Properties();
    private final Map<String, Instruction[]> scopes = new HashMap<>();

    public Program() {
        //mt = Math Target; ccl = Calls Control; sdbt = String Database Target; fdbt = Float Database Target; ddbt = Double Database Target
        regs = Map.ofEntries(
                entry("a", 0),
                entry("b", 1),
                entry("c", 2),
                entry("d", 3),
                entry("e", 4),
                entry("f", 5),
                entry("g", 6),
                entry("mt", 7),
                entry("ccl", 8),
                entry("sdbt", 9),
                entry("fdbt", 10),
                entry("ddbt", 11)
        );
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
