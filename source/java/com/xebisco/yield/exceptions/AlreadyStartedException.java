package com.xebisco.yield.exceptions;

public class AlreadyStartedException extends RuntimeException {

    public AlreadyStartedException() {
        super();
    }

    public AlreadyStartedException(String msg) {
        super(msg);
    }

}
