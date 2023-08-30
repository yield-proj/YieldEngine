package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.io.Serializable;

public class Muld implements Call, Serializable {
    @Override
    public String name() {
        return "muld";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.DOUBLE_MAP.add(Mem.DOUBLE_MAP.get(prog.bk().get(args[0])) * Mem.DOUBLE_MAP.get(prog.bk().get(args[1])));
        int i = Mem.DOUBLE_MAP.size() - 1;
        prog.bk().put(prog.regs().get("mt"), i);
    }
}
