package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Program;

import java.io.Serializable;

public class Subti implements Call, Serializable {
    @Override
    public String name() {
        return "subti";
    }

    @Override
    public void run(Program prog, int[] args) {
        prog.bk().put(prog.regs().get("mt"), prog.bk().get(args[0]) - prog.bk().get(args[1]));
    }
}
