package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Program;

import java.io.Serializable;

public final class Exit implements Call, Serializable {
    @Override
    public String name() {
        return "exit";
    }

    @Override
    public void run(Program prog, int[] args) {
        System.exit(prog.bk().get(args[0]));
    }
}
