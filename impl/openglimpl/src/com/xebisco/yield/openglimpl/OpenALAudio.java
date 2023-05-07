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

public class OpenALAudio implements AudioManager {
    @Override
    public Object loadAudio(AudioPlayer audio) {
        return null;
    }

    @Override
    public void unloadAudio(AudioPlayer audio) {

    }

    @Override
    public void play(AudioPlayer audioPlayer) {

    }

    @Override
    public void loop(AudioPlayer audioPlayer) {

    }

    @Override
    public void pause(AudioPlayer audioPlayer) {

    }

    @Override
    public double getLength(AudioPlayer audioPlayer) {
        return 0;
    }

    @Override
    public double getPosition(AudioPlayer audioPlayer) {
        return 0;
    }

    @Override
    public void setPosition(AudioPlayer audioPlayer, double position) {

    }

    @Override
    public void setGain(AudioPlayer audioPlayer, double gain) {

    }

    @Override
    public void setPan(AudioPlayer audioPlayer, double pan) {

    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        return false;
    }
}
