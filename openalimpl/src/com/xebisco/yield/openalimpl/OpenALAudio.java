/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.openalimpl;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;
import com.xebisco.yield.AudioManager;
import com.xebisco.yield.AudioPlayer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class OpenALAudio implements AudioManager {

    private static final AL al = ALFactory.getAL();
    private boolean started;

    @Override
    public Object loadAudio(AudioPlayer audio) {

        Audio out = new Audio();

        al.alGenBuffers(1, out.getBuffer(), 0);

        if (al.alGetError() != AL.AL_NO_ERROR)
            throw new OpenALException();

        al.alGenSources(1, out.getSource(), 0);

        if (al.alGetError() != AL.AL_NO_ERROR)
            throw new OpenALException();

        if (audio.getAudioClip().getFileFormat().equals("WAV")) {
            ALut.alutLoadWAVFile(audio.getAudioClip().getInputStream(), out.getFormat(), out.getData(), out.getSize(), out.getFreq(), out.getLoop());
            al.alBufferData(out.getBuffer()[0], out.getFormat()[0], out.getData()[0], out.getSize()[0], out.getFreq()[0]);
        } else {
            throw new IllegalStateException("Not supported audio extension. " + audio.getAudioClip().getFileFormat());
        }

        al.alSourcei(out.getSource()[0], al.AL_BUFFER, out.getBuffer()[0]);
        al.alSourcei(out.getSource()[0], al.AL_SOURCE_RELATIVE, AL.AL_TRUE);

        return out;
    }

    @Override
    public void unloadAudio(AudioPlayer audioPlayer) {
        al.alDeleteSources((((Audio) audioPlayer.getClipRef()).getSource())[0], null);
        al.alDeleteBuffers((((Audio) audioPlayer.getClipRef()).getBuffer())[0], null);
    }

    @Override
    public void play(AudioPlayer audioPlayer) {
        al.alSourcei((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_LOOPING, AL.AL_FALSE);
        al.alSourcePlay((((Audio) audioPlayer.getClipRef()).getSource())[0]);
    }

    @Override
    public void loop(AudioPlayer audioPlayer) {
        play(audioPlayer);
        al.alSourcei((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_LOOPING, AL.AL_TRUE);
    }

    @Override
    public void pause(AudioPlayer audioPlayer) {
        al.alSourcePause((((Audio) audioPlayer.getClipRef()).getSource())[0]);
    }

    @Override
    public double getLength(AudioPlayer audioPlayer) {
        IntBuffer bufferSize = IntBuffer.allocate(1), frequency = IntBuffer.allocate(1), bitsPerSample = IntBuffer.allocate(1), channels = IntBuffer.allocate(1);
        int bufferID = (((Audio) audioPlayer.getClipRef()).getSource())[0];
        al.alGetBufferi(bufferID, AL.AL_SIZE, bufferSize);
        al.alGetBufferi(bufferID, AL.AL_FREQUENCY, frequency);
        al.alGetBufferi(bufferID, AL.AL_CHANNELS, bitsPerSample);
        al.alGetBufferi(bufferID, AL.AL_BITS, channels);


        return (bufferSize.get()) / (frequency.get() * channels.get() * (bitsPerSample.get() / 8.));
    }

    @Override
    public double getPosition(AudioPlayer audioPlayer) {
        FloatBuffer pos = FloatBuffer.allocate(1);
        al.alGetSourcef((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_SEC_OFFSET, pos);
        return pos.get();
    }

    @Override
    public void setPosition(AudioPlayer audioPlayer, double position) {
        al.alSourcef((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_SEC_OFFSET, (float) position);
    }

    @Override
    public void setGain(AudioPlayer audioPlayer, double gain) {
        al.alSourcef((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_GAIN, (float) gain);
    }

    @Override
    public void setPan(AudioPlayer audioPlayer, double pan) {
        al.alDistanceModel(AL.AL_LINEAR_DISTANCE_CLAMPED);
        int sourceID = (((Audio) audioPlayer.getClipRef()).getSource())[0];

        al.alSourcefv(sourceID, AL.AL_POSITION, FloatBuffer.wrap(new float[]{(float) pan, 0.f, 0.f}));
        al.alSourcei(sourceID, AL.AL_SOURCE_RELATIVE, AL.AL_FALSE);
        al.alSourcef(sourceID, AL.AL_MAX_DISTANCE, 1f);
        al.alSourcef(sourceID, AL.AL_REFERENCE_DISTANCE, 0.5f);
    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        IntBuffer playing = IntBuffer.allocate(1);
        al.alGetSourcei((((Audio) audioPlayer.getClipRef()).getSource())[0], AL.AL_PLAYING, playing);
        return playing.get() == AL.AL_TRUE;
    }
}
