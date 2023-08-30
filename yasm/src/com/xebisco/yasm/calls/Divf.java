package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.io.Serializable;

public class Divf implements Call, Serializable {
    @Override
    public String name() {
        return "divf";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.FLOAT_MAP.add(Mem.FLOAT_MAP.get(prog.bk().get(args[0])) / Mem.FLOAT_MAP.get(prog.bk().get(args[1])));
        int i = Mem.FLOAT_MAP.size() - 1;
        prog.bk().put(prog.regs().get("mt"), i);
    }
}
