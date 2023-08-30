package com.xebisco.yasm;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.calls.*;

public final class AllCalls {
    public static Call[] calls() {
        return new Call[]{new Mov(), new MovL(), new Exit(), new Addi(), new Addf(), new Addd(), new Subti(), new Subtf(), new Subtd(), new Muli(), new Mulf(), new Muld(), new Divi(), new Divf(), new Divd(), new Lsdb(), new Lfdb(), new Lddb(), new Outs(), new Cls(), new MemF(), new Csi(), new Csf(), new Csd(), new Usdb(), new Ufdb(), new Uddb(), new Sv(), new Lsv()};
    }
}
