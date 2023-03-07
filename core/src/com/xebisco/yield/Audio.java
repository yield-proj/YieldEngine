package com.xebisco.yield;

import java.io.InputStream;

public class Audio extends FileInput {
    private final AudioManager audioManager;
    private Object clipRef;
    public Audio(String relativePath, AudioManager audioManager) {
        super(relativePath);
        this.audioManager = audioManager;
        clipRef = audioManager.loadAudio(this);
    }

    public Audio(InputStream inputStream, AudioManager audioManager) {
        super(inputStream);
        this.audioManager = audioManager;
        clipRef = audioManager.loadAudio(this);
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public Object getClipRef() {
        return clipRef;
    }

    public void setClipRef(Object clipRef) {
        this.clipRef = clipRef;
    }
}
