package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Program;

import java.io.Serializable;

public final class Mov implements Call, Serializable {
    @Override
    public String name() {
        return "mov";
    }

    @Override
    public void run(Program prog, int[] args) {
        prog.bk().put(args[0], prog.bk().get(args[1]));
    }
}
