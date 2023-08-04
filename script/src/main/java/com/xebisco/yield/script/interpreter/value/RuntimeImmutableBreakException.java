package com.xebisco.yield.script.interpreter.value;

public class RuntimeImmutableBreakException extends RuntimeException {
    public RuntimeImmutableBreakException() {
    }

    public RuntimeImmutableBreakException(String message) {
        super(message);
    }

    public RuntimeImmutableBreakException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeImmutableBreakException(Throwable cause) {
        super(cause);
    }

    public RuntimeImmutableBreakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
