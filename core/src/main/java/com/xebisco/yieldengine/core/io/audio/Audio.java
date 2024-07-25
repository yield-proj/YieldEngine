package com.xebisco.yieldengine.core.io.audio;

import java.io.Serializable;

public class Audio implements Serializable {
    private final Serializable audioReference;

    public Audio(Serializable audioReference) {
        this.audioReference = audioReference;
    }

    public Serializable getAudioReference() {
        return audioReference;
    }
}
