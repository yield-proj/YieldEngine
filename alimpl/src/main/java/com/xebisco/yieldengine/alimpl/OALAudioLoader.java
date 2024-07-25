package com.xebisco.yieldengine.alimpl;

import com.xebisco.yieldengine.core.io.audio.Audio;
import com.xebisco.yieldengine.core.io.audio.AudioSource;
import com.xebisco.yieldengine.core.io.audio.IAudioLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OALAudioLoader implements IAudioLoader {
    @Override
    public Audio loadAudio(String path) {
        int buffer = alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(path, info);

            //copy to buffer
            alBufferData(buffer, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
        }
        return new Audio(buffer);
    }

    private static ShortBuffer readVorbis(String path, STBVorbisInfo info) {
        IntBuffer error = BufferUtils.createIntBuffer(1);
        long decoder = stb_vorbis_open_filename(path, error, null);
        if (decoder == NULL) {
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
    public void unloadAudio(Object audioReference) {
        alDeleteBuffers((int) audioReference);
    }

    @Override
    public AudioSource loadSource() {
        return new AudioSource(alGenSources());
    }

    @Override
    public void unloadSource(Object audioSourceReference) {
        alDeleteSources((int) audioSourceReference);
    }
}
