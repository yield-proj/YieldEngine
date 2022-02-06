package com.xebisco.yield.exception;

public class AlreadyStartedException extends RuntimeException {

    public AlreadyStartedException() {
        super();
    }

    public AlreadyStartedException(String msg) {
        super(msg);
    }

}
