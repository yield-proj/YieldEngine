package com.xebisco.yasm;

public class Lsdb implements Call {

    @Override
    public String name() {
        return "lsdb";
    }

    @Override
    public void run(Program prog, int[] args) {
        String s = prog.properties().getProperty("sdb" + prog.bk().get(args[0]));
        Mem.STRING_MAP.add(s);
        prog.bk().put(prog.regs().get("sdbt"), Mem.STRING_MAP.size() - 1);
    }
}
