package com.xebisco.yield.script.compiler;

public class ValueDoesntExistsException extends Exception {
    public ValueDoesntExistsException() {
    }

    public ValueDoesntExistsException(String message) {
        super(message);
    }

    public ValueDoesntExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueDoesntExistsException(Throwable cause) {
        super(cause);
    }

    public ValueDoesntExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
