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

public interface AudioManager {
    Object loadAudio(AudioPlayer audio);

    void unloadAudio(AudioPlayer audio);

    void play(AudioPlayer audioPlayer);

    void pause(AudioPlayer audioPlayer);

    double getLength(AudioPlayer audioPlayer);

    double getPosition(AudioPlayer audioPlayer);

    void setPosition(AudioPlayer audioPlayer, double position);

    void setGain(AudioPlayer audioPlayer, double gain);

    void setPan(AudioPlayer audioPlayer, double pan);

    boolean isPlaying(AudioPlayer audioPlayer);
}

