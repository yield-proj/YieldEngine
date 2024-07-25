package com.xebisco.yieldengine.core.components;

import com.xebisco.yieldengine.core.Component;
import com.xebisco.yieldengine.core.io.IO;
import com.xebisco.yieldengine.core.io.audio.Audio;
import com.xebisco.yieldengine.core.io.audio.AudioSource;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;

public class AudioEmitter extends Component {
    private float gain = 1f, rolloffFactor = 1f;
    private boolean relative, looping;
    private final AudioSource source;
    private Audio audio;
    private boolean startPlaying;

    public AudioEmitter() {
        source = IO.getInstance().getAudioLoader().loadSource();
    }

    public AudioEmitter(Audio audio, boolean startPlaying) {
        this();
        this.audio = audio;
        this.startPlaying = startPlaying;
    }

    @Override
    public void onStart() {
        if (startPlaying) {
            updateAudio();
            play();
        }
    }

    @Override
    public void onLateUpdate() {
        updateAudio();
    }

    private void updateAudio() {
        IAudioPlayer audioPlayer = IO.getInstance().getAudioPlayer();
        audioPlayer.setSourcePosition(source, getEntity().getNewWorldTransform().getPosition());
        audioPlayer.setSourceGain(source, gain);
        audioPlayer.setSourceRolloffFactor(source, rolloffFactor);
        audioPlayer.setSourceRelative(source, relative);
        audioPlayer.setSourceLooping(source, looping);
        audioPlayer.setSourceAudio(source, audio);
    }

    @Override
    public void dispose() {
        IO.getInstance().getAudioLoader().unloadSource(source.getAudioSourceReference());
    }

    public void play() {
        IO.getInstance().getAudioPlayer().sourcePlay(source);
    }

    public void pause() {
        IO.getInstance().getAudioPlayer().sourcePause(source);
    }

    public void stop() {
        IO.getInstance().getAudioPlayer().sourceStop(source);
    }

    public boolean isPlaying() {
        return IO.getInstance().getAudioPlayer().isSourcePlaying(source);
    }

    public float getGain() {
        return gain;
    }

    public AudioEmitter setGain(float gain) {
        this.gain = gain;
        return this;
    }

    public boolean isRelative() {
        return relative;
    }

    public AudioEmitter setRelative(boolean relative) {
        this.relative = relative;
        return this;
    }

    public boolean isLooping() {
        return looping;
    }

    public AudioEmitter setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }

    public AudioSource getSource() {
        return source;
    }

    public Audio getAudio() {
        return audio;
    }

    public AudioEmitter setAudio(Audio audio) {
        this.audio = audio;
        return this;
    }

    public boolean isStartPlaying() {
        return startPlaying;
    }

    public AudioEmitter setStartPlaying(boolean startPlaying) {
        this.startPlaying = startPlaying;
        return this;
    }

    public float getRolloffFactor() {
        return rolloffFactor;
    }

    public AudioEmitter setRolloffFactor(float rolloffFactor) {
        this.rolloffFactor = rolloffFactor;
        return this;
    }
}
