package com.xebisco.yieldengine.core.io.audio;

public interface IAudioLoader {
    Audio loadAudio(String path);
    void unloadAudio(Object audioReference);
    AudioSource loadSource();
    void unloadSource(Object audioSourceReference);
}
