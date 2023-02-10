/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.render;

import com.xebisco.yield.AudioClip;
import com.xebisco.yield.AudioPlayer;

public interface AudioUtils {
    /**
     * Loads an audio clip into an audio player
     *
     * @param audioClip The AudioClip to load.
     * @param audioPlayer The AudioPlayer object that will play the audio clip.
     */
    void loadAudioClip(AudioClip audioClip, AudioPlayer audioPlayer);
    /**
     * Sets an audioPlayer position in microseconds.
     *
     * @param audioPlayer The AudioPlayer object that you want to set the position of.
     * @param pos The position in microseconds to set the position to.
     */
    void setMicrosecondPosition(AudioPlayer audioPlayer, long pos);
    /**
     * It returns the current position of an audio player in microseconds.
     *
     * @param audioPlayer The AudioPlayer object that you want to get the position of.
     * @return The current position of the audio player in microseconds.
     */
    long getMicrosecondPosition(AudioPlayer audioPlayer);
    /**
     * It returns the length of the audio player in microseconds
     *
     * @param audioPlayer The AudioPlayer object that you want to get the length of.
     * @return The length of the audio file in microseconds.
     */
    long getMicrosecondLength(AudioPlayer audioPlayer);

    /**
     * Get the volume of the audio player.
     *
     * @param audioPlayer The AudioPlayer object that you want to get the volume of.
     * @return The volume of the audio player.
     */
    float getVolume(AudioPlayer audioPlayer);

    /**
     * Set the volume of the audio player to the given value.
     *
     * @param audioPlayer The AudioPlayer object that you want to set the volume of.
     * @param value The volume to set the audio player to.
     */
    void setVolume(AudioPlayer audioPlayer, float value);

    /**
     * Pause the audio player.
     *
     * @param audioPlayer The audio player object that you want to pause.
     */
    void pausePlayer(AudioPlayer audioPlayer);

    /**
     * Resumes the audio player
     *
     * @param audioPlayer The AudioPlayer object that you want to resume.
     */
    void resumePlayer(AudioPlayer audioPlayer);

    /**
     * Unloads the audio player
     *
     * @param audioPlayer The audio player to flush.
     */
    void unloadPlayer(AudioPlayer audioPlayer);

    /**
     * Unloads all audio players
     */
    void unloadAllPlayers();

    /**
     * Sets whether the audio player should loop or not
     *
     * @param audioPlayer The AudioPlayer object that you want to set the looping for.
     * @param loop true or false
     */
    void setLoop(AudioPlayer audioPlayer, boolean loop);
    /**
     * Sets the number of times the audio should loop
     *
     * @param audioPlayer The AudioPlayer object that you want to set the loop count for.
     * @param count The number of times the audio should loop.
     */
    void setLoop(AudioPlayer audioPlayer, int count);

    /**
     * Returns true if the player is running, false otherwise
     *
     * @param audioPlayer The AudioPlayer object that you want to check.
     * @return A boolean value.
     */
    boolean isPlayerRunning(AudioPlayer audioPlayer);

    /**
     * Loads the audio player.
     *
     * @param player The AudioPlayer object to load an audio file into.
     */
    void loadAudioPlayer(AudioPlayer player);
}
