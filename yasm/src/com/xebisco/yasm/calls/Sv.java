package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.util.HashMap;
import java.util.Map;

public class Sv implements Call {
    @Override
    public String name() {
        return "sv";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.REGS_MAP.add(new HashMap<>(prog.bk()));
        prog.bk().put(args[0], Mem.REGS_MAP.size() - 1);
    }
}
