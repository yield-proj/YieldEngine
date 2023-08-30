package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Ufdb implements Call {

    @Override
    public String name() {
        return "ufdb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.FLOAT_MAP.remove(args[0]);
    }
}
