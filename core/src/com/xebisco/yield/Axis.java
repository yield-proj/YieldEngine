package com.xebisco.yield;

public class Axis {
    private final String name;
    private final Input.Key positiveKey, negativeKey, altPositiveKey, altNegativeKey;
    private double value;

    public Axis(String name, Input.Key positiveKey, Input.Key negativeKey, Input.Key altPositiveKey, Input.Key altNegativeKey) {
        this.name = name;
        this.positiveKey = positiveKey;
        this.negativeKey = negativeKey;
        this.altPositiveKey = altPositiveKey;
        this.altNegativeKey = altNegativeKey;
    }

    public Axis(String name, Input.Key positiveKey, Input.Key negativeKey) {
        this.name = name;
        this.positiveKey = positiveKey;
        this.negativeKey = negativeKey;
        this.altPositiveKey = null;
        this.altNegativeKey = null;
    }

    public String getName() {
        return name;
    }

    public Input.Key getPositiveKey() {
        return positiveKey;
    }

    public Input.Key getNegativeKey() {
        return negativeKey;
    }

    public Input.Key getAltPositiveKey() {
        return altPositiveKey;
    }

    public Input.Key getAltNegativeKey() {
        return altNegativeKey;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
