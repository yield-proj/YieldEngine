package com.xebisco.yasm;

public class UnknownCallException extends Exception {
    public UnknownCallException() {
    }

    public UnknownCallException(String message) {
        super(message);
    }

    public UnknownCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownCallException(Throwable cause) {
        super(cause);
    }

    public UnknownCallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
