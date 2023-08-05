package com.xebisco.yield.script.interpreter.value;

public class ValueDoesntExistsRuntimeException extends RuntimeException {
    public ValueDoesntExistsRuntimeException() {
    }

    public ValueDoesntExistsRuntimeException(String message) {
        super(message);
    }

    public ValueDoesntExistsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueDoesntExistsRuntimeException(Throwable cause) {
        super(cause);
    }

    public ValueDoesntExistsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
