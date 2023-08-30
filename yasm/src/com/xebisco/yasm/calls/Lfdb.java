package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Lfdb implements Call {
    @Override
    public String name() {
        return "lfdb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.FLOAT_MAP.add((float) prog.properties().get("fdb" + prog.bk().get(args[0])));
        prog.bk().put(prog.regs().get("fdbt"), Mem.FLOAT_MAP.size() - 1);
    }
}
