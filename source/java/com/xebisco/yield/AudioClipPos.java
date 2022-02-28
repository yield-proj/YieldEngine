package com.xebisco.yield;

public class AudioClipPos {
    private long position;
    private boolean microsecond;

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public boolean isMicrosecond() {
        return microsecond;
    }

    public void setMicrosecond(boolean microsecond) {
        this.microsecond = microsecond;
    }
}