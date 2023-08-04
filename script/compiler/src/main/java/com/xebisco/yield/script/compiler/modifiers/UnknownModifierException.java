package com.xebisco.yield.script.compiler.modifiers;

public class UnknownModifierException extends Exception {
    public UnknownModifierException() {
    }

    public UnknownModifierException(String message) {
        super(message);
    }

    public UnknownModifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownModifierException(Throwable cause) {
        super(cause);
    }

    public UnknownModifierException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
