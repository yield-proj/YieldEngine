package com.xebisco.yasm;

import java.io.Serializable;

public final class Instruction implements Runnable, Serializable {
    private final Call call;
    private final int[] args;
    private final Program program;

    public Instruction(Call call, int[] args, Program program) {
        this.call = call;
        this.args = args;
        this.program = program;
    }

    @Override
    public void run() {
        call.run(program, args);
    }

    public Call call() {
        return call;
    }

    public int[] args() {
        return args;
    }

    public Program program() {
        return program;
    }
}
