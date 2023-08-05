package com.xebisco.yield.script.interpreter.value;

public class ImmutableBreakRuntimeException extends RuntimeException {
    public ImmutableBreakRuntimeException() {
    }

    public ImmutableBreakRuntimeException(String message) {
        super(message);
    }

    public ImmutableBreakRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImmutableBreakRuntimeException(Throwable cause) {
        super(cause);
    }

    public ImmutableBreakRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
