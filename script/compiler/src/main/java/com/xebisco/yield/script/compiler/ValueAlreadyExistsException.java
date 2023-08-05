package com.xebisco.yield.script.compiler;

public class ValueAlreadyExistsException extends Exception {
    public ValueAlreadyExistsException() {
    }

    public ValueAlreadyExistsException(String message) {
        super(message);
    }

    public ValueAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ValueAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
