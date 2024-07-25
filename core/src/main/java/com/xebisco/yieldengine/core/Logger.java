package com.xebisco.yieldengine.core;

public final class Logger {
    private static Logger instance;

    private final boolean engineDebug, debug;

    public Logger(boolean engineDebug, boolean debug) {
        this.engineDebug = engineDebug;
        this.debug = debug;
    }

    public void log(Object x) {
        System.out.println(x);
    }

    public void debug(Object x) {
        if (debug) {
            System.err.println("(" + Time.getExecutionTimeString() + ") DEBUG: " + x);
        }
    }

    public void engineDebug(Object x) {
        if (engineDebug) {
            System.err.println("(" + Time.getExecutionTimeString() + ") ENGINE_DEBUG: " + x);
        }
    }

    public boolean isEngineDebug() {
        return engineDebug;
    }

    public boolean isDebug() {
        return debug;
    }

    public static Logger getInstance() {
        return instance;
    }

    public static void setInstance(Logger instance) {
        Logger.instance = instance;
    }
}
