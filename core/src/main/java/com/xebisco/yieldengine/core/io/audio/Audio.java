package com.xebisco.yieldengine.core.io.audio;

import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public class Audio implements Serializable {
    private final Serializable audioReference;

    public Audio(String path) {
        Audio created = IO.getInstance().loadAudio(path);
        audioReference = created.audioReference;
    }

    private Audio(Serializable audioReference) {
        this.audioReference = audioReference;
    }

    public static Audio create(Serializable audioReference) {
        return new Audio(audioReference);
    }

    public Serializable getAudioReference() {
        return audioReference;
    }
}
