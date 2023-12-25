package com.xebisco.yield;

public class OnlyModifiableBeforeCreationException extends Exception {
    public OnlyModifiableBeforeCreationException() {
    }

    public OnlyModifiableBeforeCreationException(String message) {
        super(message);
    }

    public OnlyModifiableBeforeCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlyModifiableBeforeCreationException(Throwable cause) {
        super(cause);
    }

    public OnlyModifiableBeforeCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
