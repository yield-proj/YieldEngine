package com.xebisco.yield.script.compiler;

public class ParenthesisLevelException extends Exception {
    public ParenthesisLevelException() {
    }

    public ParenthesisLevelException(String message) {
        super(message);
    }

    public ParenthesisLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParenthesisLevelException(Throwable cause) {
        super(cause);
    }

    public ParenthesisLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
