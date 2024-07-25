package com.xebisco.yieldengine.core.io.audio;

import java.io.Serializable;

public class AudioSource {
    private final Serializable audioSourceReference;

    public AudioSource(Serializable audioSourceReference) {
        this.audioSourceReference = audioSourceReference;
    }

    public Serializable getAudioSourceReference() {
        return audioSourceReference;
    }
}
