package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;
import com.xebisco.yasm.Rd;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cls implements Call, Serializable {
    @Override
    public String name() {
        return "cls";
    }

    @Override
    public void run(Program prog, int[] args) {
        Map<Integer, Integer> m = new HashMap<>(prog.bk());
        prog.bk().clear();
        prog.bk().putAll(m);
        if(prog.bk().get(args[0]) == 1) Rd.run(prog.scopes().get(Mem.STRING_MAP.get(prog.bk().get(args[1]))), prog);
        prog.bk().putAll(m);
    }
}
