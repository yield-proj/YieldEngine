package com.xebisco.yield;

public class ContextTime {
    private double timeScale = 1, deltaTime;
    private long targetSleepTime = 16;

    public double getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(double timeScale) {
        this.timeScale = timeScale;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(double deltaTime) {
        this.deltaTime = deltaTime;
    }

    public long getTargetSleepTime() {
        return targetSleepTime;
    }

    public void setTargetSleepTime(long targetSleepTime) {
        this.targetSleepTime = targetSleepTime;
    }
}
