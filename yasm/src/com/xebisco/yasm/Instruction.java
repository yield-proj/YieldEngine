package com.xebisco.yasm;

import java.io.Serializable;

public final class Instruction implements Serializable {
    private final Call call;
    private final int[] args;

    public Instruction(Call call, int[] args) {
        this.call = call;
        this.args = args;
    }

    public void run(Program program) {
        call.run(program, args);
    }

    public Call call() {
        return call;
    }

    public int[] args() {
        return args;
    }
}
