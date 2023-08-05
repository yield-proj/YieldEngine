package com.xebisco.yield.script.compiler;

import java.util.regex.Pattern;

public class CompilerPatterns {
    public static final String VARIABLE_STRING = "[^0-9][a-zA-Z0-9.]*";
    public static final Pattern ASSIGN_PATTERN = Pattern.compile("^(let|var|)(" + VARIABLE_STRING + ") *= *(.+)$"),
            CAST_PATTERN = Pattern.compile(" *as *([a-zA-Z0-9.])"),
            STRING_PATTERN = Pattern.compile("STR::([0-9]+)");
    public static final char[] ADD_LVL_CHARS = {
        '='
    };
}
