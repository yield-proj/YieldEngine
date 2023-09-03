package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Csi implements Call {
    @Override
    public String name() {
        return "csi";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.STRING_MAP.add(String.format(Mem.STRING_MAP.get(prog.bk().get(args[0])), prog.bk().get(args[1])));
        prog.bk().put(prog.regs().get("sdbt"), Mem.STRING_MAP.size() - 1);
    }
}