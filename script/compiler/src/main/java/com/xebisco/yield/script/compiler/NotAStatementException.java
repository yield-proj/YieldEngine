package com.xebisco.yield.script.compiler;

public class NotAStatementException extends Exception {
    public NotAStatementException() {
    }

    public NotAStatementException(String message) {
        super(message);
    }

    public NotAStatementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAStatementException(Throwable cause) {
        super(cause);
    }

    public NotAStatementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
