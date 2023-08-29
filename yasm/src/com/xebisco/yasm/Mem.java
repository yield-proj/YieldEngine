package com.xebisco.yasm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mem {
    public final static List<String> STRING_MAP = new ArrayList<>();
    private final Map<Integer, Integer> bk = new HashMap<>();

    public Map<Integer, Integer> bk() {
        return bk;
    }
}
