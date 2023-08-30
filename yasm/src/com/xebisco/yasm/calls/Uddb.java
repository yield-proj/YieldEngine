package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

public class Uddb implements Call {

    @Override
    public String name() {
        return "uddb";
    }

    @Override
    public void run(Program prog, int[] args) {
        Mem.DOUBLE_MAP.remove(args[0]);
    }
}
