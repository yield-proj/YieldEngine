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

package com.xebisco.yield;

public class AudioPlayer extends ComponentBehavior {
    private FileInput audioClip;
    private Object clipRef;
    private double pan;
    private double gain;

    public void play() {
        getApplication().getAudioManager().play(this);
    }

    public void pause() {
        getApplication().getAudioManager().pause(this);
    }

    @Override
    public void dispose() {
        if(audioClip != null) {
            pause();
            setPosition(0);
            getApplication().getAudioManager().unloadAudio(this);
        }
    }

    public double getPosition() {
        return getApplication().getAudioManager().getPosition(this);
    }

    public void setPosition(double position) {
        getApplication().getAudioManager().setPosition(this, position);
    }

    public double getLength() {
        return getApplication().getAudioManager().getLength(this);
    }

    public FileInput getAudioClip() {
        return audioClip;
    }

    public void setAudioClip(FileInput audioClip) {
        if (this.audioClip != null)
            getApplication().getAudioManager().unloadAudio(this);
        this.audioClip = audioClip;
        setClipRef(getApplication().getAudioManager().loadAudio(this));
    }

    public Object getClipRef() {
        return clipRef;
    }

    public void setClipRef(Object clipRef) {
        this.clipRef = clipRef;
    }

    public double getPan() {
        return pan;
    }

    public void setPan(double pan) {
        this.pan = pan;
        if (gain < -1.0 || gain > 1.0)
            throw new IllegalArgumentException("Pan not valid: " + gain);
        getApplication().getAudioManager().setPan(this, pan);
    }

    public boolean isPlaying() {
        return getApplication().getAudioManager().isPlaying(this);
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
        if (gain < 0.0 || gain > 1.0)
            throw new IllegalArgumentException("Gain not valid: " + gain);
        getApplication().getAudioManager().setGain(this, gain);
    }
}
