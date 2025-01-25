package com.xebisco.yieldengine.core.io;

public interface ILoad {
    void load();

    default void loadIfNull() {
        load();
    }
}
