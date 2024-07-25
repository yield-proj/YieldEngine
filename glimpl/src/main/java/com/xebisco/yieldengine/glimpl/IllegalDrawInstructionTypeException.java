package com.xebisco.yieldengine.glimpl;

public class IllegalDrawInstructionTypeException extends RuntimeException {
    public IllegalDrawInstructionTypeException() {
    }

    public IllegalDrawInstructionTypeException(String message) {
        super(message);
    }

    public IllegalDrawInstructionTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDrawInstructionTypeException(Throwable cause) {
        super(cause);
    }

    public IllegalDrawInstructionTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
