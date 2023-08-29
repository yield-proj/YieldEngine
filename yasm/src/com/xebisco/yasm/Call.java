package com.xebisco.yasm;

import java.io.Serializable;

public interface Call extends Serializable {
    String name();
    void run(Program prog, int[] args);
}
