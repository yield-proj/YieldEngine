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

/**
 * The AudioManager interface should be implemented by the specific platform running the application, it contains function for manipulation audio files.
 */
public interface AudioManager {
    /**
     * This function loads an audio file.
     *
     * @param audio The "audio" parameter in the "loadAudio" method is an object of the "AudioPlayer" class. This object
     * contains an audio file that needs to be loaded into the program. The "loadAudio" method is responsible for loading
     * the audio file into the program and returning its clip reference.
     * @return The audio clip reference.
     */
    Object loadAudio(AudioPlayer audio);

    /**
     * The function unloads an audio clip from an audio player.
     *
     * @param audio The "audio" parameter is an object of type "AudioPlayer" that contains an audio file that has been
     * loaded into memory. The "unloadAudio" function is used to free up the memory used by the audio file.
     */
    void unloadAudio(AudioPlayer audio);

    /**
     * The function "play" plays an audio clip from an AudioPlayer object.
     *
     * @param audioPlayer The parameter "audioPlayer" is an object of the class "AudioPlayer". It is being passed as an
     * argument to the function "play". The function will play the audio clip related to this AudioPlayer object.
     */
    void play(AudioPlayer audioPlayer);

    /**
     * This function takes an AudioPlayer object as input and pauses it.
     *
     * @param audioPlayer The audio player to be paused.
     */
    void pause(AudioPlayer audioPlayer);

    /**
     * The function "getLength" takes an AudioPlayer object as input and returns its length as a double.
     *
     * @param audioPlayer The audio player.
     * @return A double value representing the length of the audio file being hold by the AudioPlayer object passed as a
     * parameter.
     */
    double getLength(AudioPlayer audioPlayer);

    /**
     * The function returns the current position of an audio player.
     *
     * @param audioPlayer The audio player.
     * @return a double value, which is the current position of the audio player in seconds.
     */
    double getPosition(AudioPlayer audioPlayer);

    /**
     * The function sets the position of an audio player.
     *
     * @param audioPlayer The audio player.
     * @param position The position parameter is a double value that represents the desired position in the audio file
     * being played by the AudioPlayer. It is measured in seconds from the beginning of the audio file. By setting
     * the position parameter, you can move the playback position of the audio file to a specific point in time.
     */
    void setPosition(AudioPlayer audioPlayer, double position);

    /**
     * The function sets the gain of an audio player.
     *
     * @param audioPlayer The audioPlayer parameter is an object of the AudioPlayer class. It represents the audio player
     * that you want to set the gain for.
     * @param gain The gain parameter in the setGain() function is a double value that represents the amount of
     * amplification to be applied to the audio signal.
     */
    void setGain(AudioPlayer audioPlayer, double gain);

    /**
     * The setPan function sets the stereo panning of an audio player.
     *
     * @param audioPlayer The audioPlayer parameter is an object of the AudioPlayer class. It represents the audio player
     * that you want to set the pan for.
     * @param pan The "pan" parameter in the "setPan" method is a double value that represents the stereo panning of the
     * audio player.
     */
    void setPan(AudioPlayer audioPlayer, double pan);

    /**
     * The function checks if an audio player is currently playing.
     *
     * @param audioPlayer The audio player.
     * @return A boolean value is being returned.
     */
    boolean isPlaying(AudioPlayer audioPlayer);
}

