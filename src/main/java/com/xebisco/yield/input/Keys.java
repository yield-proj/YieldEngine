package com.xebisco.yield.input;

import java.util.Arrays;

public class Keys {
    public final Integer[] keyCode;
    public Keys(Integer keyCode) {
        this.keyCode = new Integer[1];
        this.keyCode[0] = keyCode;
    }

    public Keys(Integer[] keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Keys keys = (Keys) o;
        return Arrays.equals(keyCode, keys.keyCode);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keyCode);
    }
}
