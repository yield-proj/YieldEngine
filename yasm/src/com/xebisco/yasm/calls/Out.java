package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Mem;
import com.xebisco.yasm.Program;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class Out implements Call, Serializable {
    @Override
    public String name() {
        return "out";
    }

    @Override
    public void run(Program prog, int[] args) {
        int ccl = prog.bk().get(prog.regs().get("ccl"));
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
            os.write(Mem.STRING_MAP.get(prog.bk().get(args[0])).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
