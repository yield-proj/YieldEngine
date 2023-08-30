package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Lddb implements Call {
    @Override
    public String name() {
        return "lddb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.DOUBLE_MAP.add((double) prog.properties().get("ddb" + prog.bk().get(args[0])));
        prog.bk().put(prog.regs().get("ddbt"), Mem.DOUBLE_MAP.size() - 1);
    }
}
