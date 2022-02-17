package com.xebisco.yield.script.console;

import com.xebisco.yield.script.ScriptFunction;
import com.xebisco.yield.script.ScriptUtils;
import com.xebisco.yield.script.YldScript;

public class Log extends ScriptFunction {
    public Log(YldScript script) {
        super("log", null, script);
    }

    @Override
    public void call(String[] arguments) {
        for(String toPrint : arguments) {
            System.out.println(ScriptUtils.getVar(toPrint, getScript()));
        }
    }
}
