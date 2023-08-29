package com.xebisco.yasm;

import java.io.Serializable;

public record Instruction(Call call, int[] args) implements Serializable {

    public void run(Program program) {
        call.run(program, args);
    }
}
