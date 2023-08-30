package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class Outs implements Call, Serializable {
    @Override
    public String name() {
        return "outs";
    }

    @Override
    public void run(Program prog, int[] args) {
        int ccl = prog.bk().get(args[0]);
        OutputStream os;
        switch (ccl) {
            case 0 -> {
                os = System.out;
            }
            default -> {
                throw new IllegalArgumentException(String.valueOf(ccl));
            }
        }
        try {
            os.write(Mem.STRING_MAP.get(prog.bk().get(args[1])).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
