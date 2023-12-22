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

import java.io.IOException;

/**
 * The AudioPlayer class is a component behavior that allows for playing, pausing, and manipulating audio clips in a Yield
 * application.
 */
public class AudioPlayer extends ComponentBehavior {
    private FileInput audioClip;
    private Object clipRef;
    private double pan;
    private double gain;

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
        if(audioClip != null) {
            pause();
            setPosition(0);
            application().applicationPlatform().audioManager().unloadAudio(this);
        }
    }

    /**
     * This function returns the position of the audio player.
     *
     * @return The method `position()` is returning a `double` value which represents the current position of the audio
     * clip. The position is obtained from the `AudioManager` of this application.
     */
    public double position() {
        return application().applicationPlatform().audioManager().getPosition(this);
    }

    /**
     * This function sets the position of the audio player using the application's audio manager.
     *
     * @param position The parameter "position" is a double value that represents the new position of the audio playback in
     * seconds. This method is used to set the position of the audio playback to a specific time in the audio file.
     */
    public AudioPlayer setPosition(double position) {
        application().applicationPlatform().audioManager().setPosition(this, position);
        return this;
    }

    /**
     * This function returns the length of the audio clip.
     *
     * @return The method `length()` is returning a `double` value which represents the length of the audio clip.
     */
    public double length() {
        return application().applicationPlatform().audioManager().getLength(this);
    }

    /**
     * The function returns an audio clip file input.
     *
     * @return The method is returning a FileInput object named "audioClip".
     */
    public FileInput audioClip() {
        return audioClip;
    }

    /**
     * This function sets an audio clip and unloads any previous audio clip if it exists.
     *
     * @param audioClip The audio file that is being set for the audio player.
     */
    public AudioPlayer setAudioClip(FileInput audioClip) {
        if (this.audioClip != null)
            application().applicationPlatform().audioManager().unloadAudio(this);
        this.audioClip = audioClip;
        setClipRef(application().applicationPlatform().audioManager().loadAudio(this));
        return this;
    }

    /**
     * The function returns the reference to a clip object.
     *
     * @return The method is returning the value of the variable `clipRef`, which is of type `Object`.
     */
    public Object clipRef() {
        return clipRef;
    }

    /**
     * This function sets the value of the clipRef variable.
     *
     * @param clipRef The clipRef value to set.
     */
    public AudioPlayer setClipRef(Object clipRef) {
        this.clipRef = clipRef;
        return this;
    }

    /**
     * The function returns the value of the "pan" variable as a double data type.
     *
     * @return The method `pan()` is returning a `double` value which is the value of the variable `pan`.
     */
    public double pan() {
        return pan;
    }

    /**
     * This function sets the pan of an audio object and throws an exception if the gain is not within the valid range.
     *
     * @param pan The parameter "pan" is a double value representing the stereo panning of the audio. A value of -1.0 means
     * the audio is fully panned to the left channel, 0.0 means the audio is centered, and 1.0 means the audio is fully
     * panned to the right channel.
     */
    public AudioPlayer setPan(double pan) {
        this.pan = pan;
        if (pan < -1.0 || pan > 1.0)
            throw new IllegalArgumentException("Pan not valid: " + gain);
        application().applicationPlatform().audioManager().setPan(this, pan);
        return this;
    }

    /**
     * This function returns a boolean value indicating whether the audio is currently playing or not.
     *
     * @return The method `playing()` is returning a boolean value. It is returning `true` if the audio is currently
     * playing and `false` if it is not playing.
     */
    public boolean playing() {
        return application().applicationPlatform().audioManager().isPlaying(this);
    }

    /**
     * The function returns the value of the variable "gain" as a double.
     *
     * @return The method is returning a double value, which is the value of the variable "gain".
     */
    public double gain() {
        return gain;
    }

    /**
     * This function sets the gain of the audio player and throws an exception if the gain is not within the valid range.
     *
     * @param gain The gain parameter is a double value representing the audio gain level to be set. It should be between
     * 0.0 and 1.0, where 0.0 means no sound and 1.0 means the maximum volume.
     */
    public void gain(double gain) {
        this.gain = gain;
        if (gain < 0.0 || gain > 1.0)
            throw new IllegalArgumentException("Gain not valid: " + gain);
        application().applicationPlatform().audioManager().setGain(this, gain);
    }
}
