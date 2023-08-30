package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Lsdb implements Call {

    @Override
    public String name() {
        return "lsdb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.STRING_MAP.add(prog.properties().getProperty("sdb" + prog.bk().get(args[0])));
        prog.bk().put(prog.regs().get("sdbt"), Mem.STRING_MAP.size() - 1);
    }
}
