package com.xebisco.yieldengine.core.input;

public final class Axis {
    public static final Axis
            HORIZONTAL = new Axis(Key.VK_D, Key.VK_A, Key.VK_RIGHT, Key.VK_LEFT),
            VERTICAL = new Axis(Key.VK_W, Key.VK_S, Key.VK_UP, Key.VK_DOWN),
            JUMP = new Axis(Key.VK_SPACE);

    private final Key positiveKey, negativeKey, altPositiveKey, altNegativeKey;

    public Axis(Key positiveKey, Key negativeKey, Key altPositiveKey, Key altNegativeKey) {
        this.positiveKey = positiveKey;
        this.negativeKey = negativeKey;
        this.altPositiveKey = altPositiveKey;
        this.altNegativeKey = altNegativeKey;
    }

    public Axis(Key positiveKey, Key negativeKey) {
        this(positiveKey, negativeKey, null, null);
    }

    public Axis(Key positiveKey) {
        this(positiveKey, null);
    }

    public float getValue() {
        Input input = Input.getInstance();
        return (positiveKey != null && input.isKeyPressed(positiveKey)) || (altPositiveKey != null && input.isKeyPressed(altPositiveKey)) ? 1.0f : (negativeKey != null && input.isKeyPressed(negativeKey)) || (altNegativeKey != null && input.isKeyPressed(altNegativeKey)) ? -1.0f : 0.0f;
    }

    public Key getPositiveKey() {
        return positiveKey;
    }

    public Key getNegativeKey() {
        return negativeKey;
    }

    public Key getAltPositiveKey() {
        return altPositiveKey;
    }

    public Key getAltNegativeKey() {
        return altNegativeKey;
    }
}
