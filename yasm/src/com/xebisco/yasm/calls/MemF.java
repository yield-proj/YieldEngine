package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Program;

public class MemF implements Call {
    @Override
    public String name() {
        return "memf";
    }

    @Override
    public void run(Program prog, int[] args) {
        prog.bk().remove(args[0]);
    }
}
