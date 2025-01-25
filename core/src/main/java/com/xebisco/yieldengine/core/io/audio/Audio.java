package com.xebisco.yieldengine.core.io.audio;

import com.xebisco.yieldengine.core.IDispose;
import com.xebisco.yieldengine.core.io.ILoad;
import com.xebisco.yieldengine.core.io.IO;

import java.io.Serializable;

public class Audio implements Serializable, IDispose, ILoad {
    private transient Serializable audioReference;
    private final String path;

    private Audio(Serializable audioReference) {
        this.path = null;
        this.audioReference = audioReference;
    }

    public void load() {
        Audio created = IO.getInstance().loadAudio(path);
        audioReference = created.audioReference;
    }

    @Override
    public void loadIfNull() {
        if(audioReference == null) {
            load();
        }
    }

    public static Audio create(Serializable audioReference) {
        return new Audio(audioReference);
    }

    @Override
    public void dispose() {
        IO.getInstance().unloadAudio(this);
    }

    public Serializable getAudioReference() {
        return audioReference;
    }

    public String getPath() {
        return path;
    }
}
