package com.xebisco.yield;

public class AudioClip extends RelativeFile {

    private AudioClipPos startPos = new AudioClipPos();

    public AudioClip(String relativePath) {
        super(relativePath);
    }

    public AudioClipPos getStartPos() {
        return startPos;
    }

    public void setStartPos(AudioClipPos startPos) {
        this.startPos = startPos;
    }
}
