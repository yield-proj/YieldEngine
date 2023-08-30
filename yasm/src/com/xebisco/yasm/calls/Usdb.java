package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Usdb implements Call {

    @Override
    public String name() {
        return "usdb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.STRING_MAP.remove(args[0]);
    }
}
