package com.xebisco.yield.script;

import java.util.Arrays;

public class ScriptFunction {
    private final String name;
    private final String[] contents;
    private final YldScript script;

    public ScriptFunction(String name, String[] contents, YldScript script) {
        this.name = name;
        this.contents = contents;
        this.script = script;
    }

    public void call(String[] arguments) {
        for(String line : contents) {
            String[] wds = line.split(" ");
            ScriptUtils.executeFunction(script, wds[0], Arrays.copyOfRange(wds, 1, wds.length));
        }
    }

    public String getName() {
        return name;
    }

    public String[] getContents() {
        return contents;
    }

    public YldScript getScript() {
        return script;
    }

    @Override
    public String toString() {
        return "ScriptFunction{" +
                "name='" + name + '\'' +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }
}
