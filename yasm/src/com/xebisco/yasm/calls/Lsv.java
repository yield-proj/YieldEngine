package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.util.HashMap;

public class Lsv implements Call {
    @Override
    public String name() {
        return "lsv";
    }

    @Override
    public void run(Program prog, int[] args) {
       prog.bk().putAll(Mem.REGS_MAP.get(prog.bk().get(args[0])));
    }
}
