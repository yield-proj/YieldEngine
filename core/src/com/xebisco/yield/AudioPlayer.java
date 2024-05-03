/*
 * Copyright [2022-2024] [Xebisco]
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

import com.xebisco.yield.editor.annotations.FileExtensions;
import com.xebisco.yield.editor.annotations.Visible;

import java.io.IOException;

/**
 * The AudioPlayer class is a component behavior that allows for playing, pausing, and manipulating audio clips in a Yield
 * application.
 */
public class AudioPlayer extends ComponentBehavior {
    @Visible
    @FileExtensions(extensions = {"wav", "ogg"})
    private FileInput audioClip;
    private Object clipRef;
    @Visible
    private double pan;
    @Visible
    private double gain = 1;

    /**
     * This function plays the audio clip using the application's audio manager.
     */
    public void play() {
        application().applicationPlatform().audioManager().play(this);
    }

    /**
     * This function loops audio using the application's audio manager.
     */
    public void loop() {
        application().applicationPlatform().audioManager().loop(this);
    }

    /**
     * The "reset" function sets the position to 0.
     */
    public void reset() {
        setPosition(0);
    }

    /**
     * This function pauses the audio clip using the application's audio manager.
     */
    public void pause() {
        application().applicationPlatform().audioManager().pause(this);
    }

    @Override
    public void close() throws IOException {
        if (audioClip!= null) {
            pause();
            setPosition(0);
            application().applicationPlatform().audioManager().unloadAudio(this, application().applicationPlatform().ioManager());
        }
    }

    /**
     * This function returns the position of the audio player.
     *
     * @return The current position of the audio clip in seconds.
     */
    public double position() {
        return application().applicationPlatform().audioManager().getPosition(this);
    }

    /**
     * This function sets the position of the audio player using the application's audio manager.
     *
     * @param position The new position of the audio playback in seconds.
     * @return The AudioPlayer instance for method chaining.
     */
    public AudioPlayer setPosition(double position) {
        application().applicationPlatform().audioManager().setPosition(this, position);
        return this;
    }

    /**
     * This function returns the length of the audio clip.
     *
     * @return The length of the audio clip in seconds.
     */
    public double length() {
        return application().applicationPlatform().audioManager().getLength(this);
    }

    /**
     * The function returns the audio clip file input.
     *
     * @return The FileInput object representing the audio clip.
     */
    public FileInput audioClip() {
        return audioClip;
    }

    /**
     * This function sets the audio clip and unloads any previous audio clip if it exists.
     *
     * @param audioClip The new audio file to be set for the audio player.
     * @return The AudioPlayer instance for method chaining.
     */
    public AudioPlayer setAudioClip(FileInput audioClip) {
        if (this.audioClip!= null) application().applicationPlatform().audioManager().unloadAudio(this, application().applicationPlatform().ioManager());
        this.audioClip = audioClip;
        setClipRef(application().applicationPlatform().audioManager().loadAudio(this, application().applicationPlatform().ioManager()));
        return this;
    }

    /**
     * The function returns the reference to a clip object.
     *
     * @return The reference to the clip object.
     */
    public Object clipRef() {
        return clipRef;
    }

    /**
     * This function sets the value of the clipRef variable.
     *
     * @param clipRef The new value for the clipRef variable.
     * @return The AudioPlayer instance for method chaining.
     */
    public AudioPlayer setClipRef(Object clipRef) {
        this.clipRef = clipRef;
        return this;
    }

    /**
     * The function returns the value of the "pan" variable as a double data type.
     *
     * @return The current stereo panning value of the audio.
     */
    public double pan() {
        return pan;
    }

    /**
     * This function sets the pan of an audio object and throws an exception if the pan is not within the valid range.
     *
     * @param pan The new stereo panning value of the audio. It should be between -1.0 (fully panned to the left) and 1.0
     *            (fully panned to the right).
     * @return The AudioPlayer instance for method chaining.
     */
    public AudioPlayer setPan(double pan) {
        this.pan = pan;
        if (pan < -1.0 || pan > 1.0) throw new IllegalArgumentException("Pan not valid: " + gain);
        application().applicationPlatform().audioManager().setPan(this, pan);
        return this;
    }

    /**
     * This function returns a boolean value indicating whether the audio is currently playing or not.
     *
     * @return True if the audio is currently playing, false otherwise.
     */
    public boolean playing() {
        return application().applicationPlatform().audioManager().isPlaying(this);
    }

    /**
     * The function returns the value of the variable "gain" as a double.
     *
     * @return The current audio gain level.
     */
    public double gain() {
        return gain;
    }

    /**
     * This function sets the gain of the audio player and throws an exception if the gain is not within the valid range.
     *
     * @param gain The new audio gain level to be set. It should be between 0.0 (no sound) and 1.0 (maximum volume).
     */
    public void gain(double gain) {
        this.gain = gain;
        if (gain < 0.0 || gain > 1.0) throw new IllegalArgumentException("Gain not valid: " + gain);
        application().applicationPlatform().audioManager().setGain(this, gain);
    }
}
