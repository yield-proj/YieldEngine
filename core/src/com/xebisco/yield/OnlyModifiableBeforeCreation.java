package com.xebisco.yield;

public class OnlyModifiableBeforeCreation extends Exception {
    public OnlyModifiableBeforeCreation() {
    }

    public OnlyModifiableBeforeCreation(String message) {
        super(message);
    }

    public OnlyModifiableBeforeCreation(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlyModifiableBeforeCreation(Throwable cause) {
        super(cause);
    }

    public OnlyModifiableBeforeCreation(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
