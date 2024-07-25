package com.xebisco.yieldengine.alimpl;

import com.xebisco.yieldengine.core.io.audio.Audio;
import com.xebisco.yieldengine.core.io.audio.AudioSource;
import com.xebisco.yieldengine.core.io.audio.IAudioPlayer;
import org.joml.Vector3fc;

import static org.lwjgl.openal.AL10.*;

public class OALAudioPlayer implements IAudioPlayer {
    @Override
    public void setSourceAudio(AudioSource source, Audio audio) {
        if (audio == null)
            alSourcei((int) source.getAudioSourceReference(), AL_BUFFER, 0);
        else alSourcei((int) source.getAudioSourceReference(), AL_BUFFER, (int) audio.getAudioReference());
    }

    @Override
    public void setSourcePosition(AudioSource source, Vector3fc position) {
        alSource3f((int) source.getAudioSourceReference(), AL_POSITION, position.x(), position.y(), position.z());
    }

    @Override
    public void setSourceGain(AudioSource source, float gain) {
        alSourcef((int) source.getAudioSourceReference(), AL_GAIN, gain);
    }

    @Override
    public void setSourceLooping(AudioSource source, boolean looping) {
        alSourcei((int) source.getAudioSourceReference(), AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
    }

    @Override
    public void setSourceRelative(AudioSource source, boolean relative) {
        alSourcei((int) source.getAudioSourceReference(), AL_SOURCE_RELATIVE, relative ? AL_TRUE : AL_FALSE);
    }

    @Override
    public void setSourceRolloffFactor(AudioSource source, float rolloffFactor) {
        alSourcef((int) source.getAudioSourceReference(), AL_ROLLOFF_FACTOR, rolloffFactor);
    }

    @Override
    public void sourcePlay(AudioSource source) {
        alSourcePlay((int) source.getAudioSourceReference());
    }

    @Override
    public void sourcePause(AudioSource source) {
        alSourcePause((int) source.getAudioSourceReference());
    }

    @Override
    public void sourceStop(AudioSource source) {
        alSourceStop((int) source.getAudioSourceReference());
    }

    @Override
    public boolean isSourcePlaying(AudioSource source) {
        return alGetSourcei((int) source.getAudioSourceReference(), AL_SOURCE_STATE) == AL_PLAYING;
    }

    @Override
    public void setListenerPosition(Vector3fc position) {
        alListener3f(AL_POSITION, position.x(), position.y(), position.z());
    }

    @Override
    public void setListenerOrientation(Vector3fc at, Vector3fc up) {
        float[] data = new float[6];
        data[0] = at.x();
        data[1] = at.y();
        data[2] = at.z();
        data[3] = up.x();
        data[4] = up.y();
        data[5] = up.z();
        alListenerfv(AL_ORIENTATION, data);
    }
}
