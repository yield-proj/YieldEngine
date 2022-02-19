package com.xebisco.yield;

import java.util.Random;

public final class Yld {

    public static final String VERSION = "4 beta3";
    public static final Random rand = new Random();
    public static void log(Object msg) {
        System.out.println(msg);
    }
    public static void exit() {
        System.exit(0);
    }

}
