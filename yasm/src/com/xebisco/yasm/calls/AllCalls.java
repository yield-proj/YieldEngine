package com.xebisco.yasm.calls;

import com.xebisco.yasm.Call;
import com.xebisco.yasm.Lsdb;

public final class AllCalls {
    public static Call[] calls() {
        return new Call[]{new Mov(), new MovL(), new Exit(), new Add(), new Lsdb(), new Out(), new Cls()};
    }
}
