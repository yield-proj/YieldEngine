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
import com.xebisco.yield.MultiThread;
import com.xebisco.yield.YldB;

public interface AudioUtils {
    void loadAudioClip(AudioClip audioClip, AudioPlayer audioPlayer, MultiThread multiThread, YldB yldB);
    void setMicrosecondPosition(AudioPlayer audioPlayer, long pos);
    long getMicrosecondPosition(AudioPlayer audioPlayer);
    long getMicrosecondLength(AudioPlayer audioPlayer);

    float getVolume(AudioPlayer audioPlayer);

    void setVolume(AudioPlayer audioPlayer, float value);

    void pausePlayer(AudioPlayer audioPlayer);

    void resumePlayer(AudioPlayer audioPlayer);

    void flushPlayer(AudioPlayer audioPlayer);

    void setLoop(AudioPlayer audioPlayer, boolean loop);
    void setLoop(AudioPlayer audioPlayer, int count);

    boolean isPlayerRunning(AudioPlayer audioPlayer);

    void loadAudioPlayer(AudioPlayer player);
}
