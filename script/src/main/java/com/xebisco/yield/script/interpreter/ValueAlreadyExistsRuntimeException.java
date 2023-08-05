package com.xebisco.yield.script.interpreter;

public class ValueAlreadyExistsRuntimeException extends RuntimeException {
    public ValueAlreadyExistsRuntimeException() {
    }

    public ValueAlreadyExistsRuntimeException(String message) {
        super(message);
    }

    public ValueAlreadyExistsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueAlreadyExistsRuntimeException(Throwable cause) {
        super(cause);
    }

    public ValueAlreadyExistsRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
