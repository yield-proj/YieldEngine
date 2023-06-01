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

package com.xebisco.yield.openglimpl;

import com.xebisco.yield.AudioManager;
import com.xebisco.yield.AudioPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC10.alcOpenDevice;
import static org.lwjgl.stb.STBVorbis.*;

public class OpenALAudio implements AudioManager {

    private boolean started;

    @Override
    public Object loadAudio(AudioPlayer audio) {
        if (!started) {
            started = false;
            long device = alcOpenDevice((ByteBuffer)null);
            ALCCapabilities deviceCaps = ALC.createCapabilities(device);

            long context = ALC10.alcCreateContext(device, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(context);
            AL.createCapabilities(deviceCaps);
        }
        Audio out = new Audio(alGenBuffers(), alGenSources());

        switch (audio.getAudioClip().getFileFormat()) {
            case "WAV":
                WaveData waveData = WaveData.create(audio.getAudioClip().getInputStream());
                assert waveData != null;
                alBufferData(out.getBuffer(), waveData.format, waveData.data, waveData.samplerate);
                break;
            case "OGG":
                try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
                    ShortBuffer pcm = readVorbis(audio.getAudioClip().getInputStream(), info);

                    alBufferData(out.getBuffer(), info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
                }
                break;
            default:
                throw new IllegalStateException("Not supported audio extension. " + audio.getAudioClip().getFileFormat());
        }

        alSourcei(out.getSource(), AL_BUFFER, out.getBuffer());
        alSourcei(out.getSource(), AL_SOURCE_RELATIVE, AL_TRUE);

        return out;
    }

    private static ShortBuffer readVorbis(InputStream inputStream, STBVorbisInfo info) {
        IntBuffer error = BufferUtils.createIntBuffer(1);
        long decoder;
        try {
            decoder = stb_vorbis_open_memory(IOUtil.fromInputStream(inputStream), error, null);
        } catch (OGLImplIOException e) {
            throw new RuntimeException(e);
        }
        if (decoder == 0) {
            throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
        }

        stb_vorbis_get_info(decoder, info);

        int channels = info.channels();

        ShortBuffer pcm = BufferUtils.createShortBuffer(stb_vorbis_stream_length_in_samples(decoder) * channels);

        stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
        stb_vorbis_close(decoder);

        return pcm;
    }

    @Override
    public void unloadAudio(AudioPlayer audioPlayer) {
        alDeleteSources(((Audio) audioPlayer.getClipRef()).getSource());
        alDeleteBuffers(((Audio) audioPlayer.getClipRef()).getBuffer());
    }

    @Override
    public void play(AudioPlayer audioPlayer) {
        alSourcei(((Audio) audioPlayer.getClipRef()).getSource(), AL_LOOPING, AL_FALSE);
        alSourcePlay(((Audio) audioPlayer.getClipRef()).getSource());
    }

    @Override
    public void loop(AudioPlayer audioPlayer) {
        play(audioPlayer);
        alSourcei(((Audio) audioPlayer.getClipRef()).getSource(), AL_LOOPING, AL_TRUE);
    }

    @Override
    public void pause(AudioPlayer audioPlayer) {
        alSourcePause(((Audio) audioPlayer.getClipRef()).getSource());
    }

    @Override
    public double getLength(AudioPlayer audioPlayer) {
        int bufferID, bufferSize, frequency, bitsPerSample, channels;
        bufferID = ((Audio) audioPlayer.getClipRef()).getBuffer();
        bufferSize = alGetBufferi(bufferID, AL_SIZE);
        frequency = alGetBufferi(bufferID, AL_FREQUENCY);
        channels = alGetBufferi(bufferID, AL_CHANNELS);
        bitsPerSample = alGetBufferi(bufferID, AL_BITS);


        return (bufferSize) / (frequency * channels * (bitsPerSample / 8.));
    }

    @Override
    public double getPosition(AudioPlayer audioPlayer) {
        return alGetSourcef(((Audio) audioPlayer.getClipRef()).getSource(), AL_SEC_OFFSET);
    }

    @Override
    public void setPosition(AudioPlayer audioPlayer, double position) {
        alSourcef(((Audio) audioPlayer.getClipRef()).getSource(), AL_SEC_OFFSET, (float) position);
    }

    @Override
    public void setGain(AudioPlayer audioPlayer, double gain) {
        alSourcef(((Audio) audioPlayer.getClipRef()).getSource(), AL_GAIN, (float) gain);
    }

    @Override
    public void setPan(AudioPlayer audioPlayer, double pan) {
        alDistanceModel(AL_LINEAR_DISTANCE_CLAMPED);
        int sourceID = ((Audio) audioPlayer.getClipRef()).getBuffer();

        alSourcefv(sourceID, AL_POSITION, new float[]{(float) pan, 0.f, 0.f});
        alSourcei(sourceID, AL_SOURCE_RELATIVE, AL_FALSE);
        alSourcef(sourceID, AL_MAX_DISTANCE, 1f);
        alSourcef(sourceID, AL_REFERENCE_DISTANCE, 0.5f);
    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        return alGetSourcei(((Audio) audioPlayer.getClipRef()).getSource(), AL_PLAYING) == AL_TRUE;
    }
}
