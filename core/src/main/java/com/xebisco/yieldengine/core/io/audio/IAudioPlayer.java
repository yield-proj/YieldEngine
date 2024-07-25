package com.xebisco.yieldengine.core.io.audio;

import org.joml.Vector3fc;

public interface IAudioPlayer {
    void setSourceAudio(AudioSource source, Audio audio);
    void setSourcePosition(AudioSource source, Vector3fc position);
    void setSourceGain(AudioSource source, float gain);
    void setSourceLooping(AudioSource source, boolean looping);
    void setSourceRelative(AudioSource source, boolean relative);
    void setSourceRolloffFactor(AudioSource source, float rolloffFactor);
    void sourcePlay(AudioSource source);
    void sourcePause(AudioSource source);
    void sourceStop(AudioSource source);
    boolean isSourcePlaying(AudioSource source);

    void setListenerPosition(Vector3fc position);
    void setListenerOrientation(Vector3fc at, Vector3fc up);
}
