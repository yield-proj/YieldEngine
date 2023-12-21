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

import com.xebisco.yield.AudioPlayer;
import com.xebisco.yield.manager.AudioManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class OpenALAudio implements AudioManager {

    private boolean started;

    @Override
    public Object loadAudio(AudioPlayer audio) {
        if (!started) {
            started = true;
            long device = ALC10.alcOpenDevice((ByteBuffer)null);
            ALCCapabilities deviceCaps = ALC.createCapabilities(device);

            long context = ALC10.alcCreateContext(device, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(context);
            AL.createCapabilities(deviceCaps);
        }
        Audio out = new Audio(AL10.alGenBuffers(), AL10.alGenSources());

        switch (audio.audioClip().fileFormat()) {
            case "WAV":
                WaveData waveData = WaveData.create(new File(audio.audioClip().path()));
                assert waveData != null;
                AL10.alBufferData(out.getBuffer(), waveData.format, waveData.data, waveData.samplerate);
                break;
            case "OGG":
                try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
                    ShortBuffer pcm = readVorbis(new BufferedInputStream(new BufferedInputStream(new FileInputStream(audio.audioClip().path()))), info);

                    AL10.alBufferData(out.getBuffer(), info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new IllegalStateException("Not supported audio extension. " + audio.audioClip().fileFormat());
        }

        AL10.alSourcei(out.getSource(), AL10.AL_BUFFER, out.getBuffer());
        AL10.alSourcei(out.getSource(), AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);

        return out;
    }

    public static ByteBuffer fromInputStream(InputStream inputStream) throws IOException {
        byte[] bytes;
        bytes = inputStream.readAllBytes();

        ByteBuffer buffer = MemoryUtil.memAlloc(bytes.length);
        for (byte b : bytes)
            buffer.put(b);

        buffer.flip();

        return buffer;
    }

    private static ShortBuffer readVorbis(InputStream inputStream, STBVorbisInfo info) {
        IntBuffer error = BufferUtils.createIntBuffer(1);
        long decoder;
        try {
            decoder = STBVorbis.stb_vorbis_open_memory(fromInputStream(inputStream), error, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (decoder == 0) {
            throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
        }

        STBVorbis.stb_vorbis_get_info(decoder, info);

        int channels = info.channels();

        ShortBuffer pcm = BufferUtils.createShortBuffer(STBVorbis.stb_vorbis_stream_length_in_samples(decoder) * channels);

        STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
        STBVorbis.stb_vorbis_close(decoder);

        return pcm;
    }

    @Override
    public void unloadAudio(AudioPlayer audioPlayer) {
        AL10.alDeleteSources(((Audio) audioPlayer.clipRef()).getSource());
        AL10.alDeleteBuffers(((Audio) audioPlayer.clipRef()).getBuffer());
    }

    @Override
    public void play(AudioPlayer audioPlayer) {
        AL10.alSourcei(((Audio) audioPlayer.clipRef()).getSource(), AL10.AL_LOOPING, AL10.AL_FALSE);
        AL10.alSourcePlay(((Audio) audioPlayer.clipRef()).getSource());
    }

    @Override
    public void loop(AudioPlayer audioPlayer) {
        play(audioPlayer);
        AL10.alSourcei(((Audio) audioPlayer.clipRef()).getSource(), AL10.AL_LOOPING, AL10.AL_TRUE);
    }

    @Override
    public void pause(AudioPlayer audioPlayer) {
        AL10.alSourcePause(((Audio) audioPlayer.clipRef()).getSource());
    }

    @Override
    public double getLength(AudioPlayer audioPlayer) {
        int bufferID, bufferSize, frequency, bitsPerSample, channels;
        bufferID = ((Audio) audioPlayer.clipRef()).getBuffer();
        bufferSize = AL10.alGetBufferi(bufferID, AL10.AL_SIZE);
        frequency = AL10.alGetBufferi(bufferID, AL10.AL_FREQUENCY);
        channels = AL10.alGetBufferi(bufferID, AL10.AL_CHANNELS);
        bitsPerSample = AL10.alGetBufferi(bufferID, AL10.AL_BITS);


        return (bufferSize) / (frequency * channels * (bitsPerSample / 8.));
    }

    @Override
    public double getPosition(AudioPlayer audioPlayer) {
        return AL10.alGetSourcef(((Audio) audioPlayer.clipRef()).getSource(), AL11.AL_SEC_OFFSET);
    }

    @Override
    public void setPosition(AudioPlayer audioPlayer, double position) {
        AL10.alSourcef(((Audio) audioPlayer.clipRef()).getSource(), AL11.AL_SEC_OFFSET, (float) position);
    }

    @Override
    public void setGain(AudioPlayer audioPlayer, double gain) {
        AL10.alSourcef(((Audio) audioPlayer.clipRef()).getSource(), AL10.AL_GAIN, (float) gain);
    }

    @Override
    public void setPan(AudioPlayer audioPlayer, double pan) {
        AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        int sourceID = ((Audio) audioPlayer.clipRef()).getBuffer();

        AL10.alSourcefv(sourceID, AL10.AL_POSITION, new float[]{(float) pan, 0.f, 0.f});
        AL10.alSourcei(sourceID, AL10.AL_SOURCE_RELATIVE, AL10.AL_FALSE);
        AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, 1f);
        AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, 0.5f);
    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        return AL10.alGetSourcei(((Audio) audioPlayer.clipRef()).getSource(), AL10.AL_PLAYING) == AL10.AL_TRUE;
    }
}
