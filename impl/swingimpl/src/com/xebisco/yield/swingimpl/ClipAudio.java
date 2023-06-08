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

package com.xebisco.yield.swingimpl;

import com.xebisco.yield.AudioManager;
import com.xebisco.yield.AudioPlayer;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class ClipAudio implements AudioManager {
    @Override
    public Object loadAudio(AudioPlayer audioPlayer) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        BufferedInputStream bis = new BufferedInputStream(audioPlayer.getAudioClip().getInputStream());
        try {
            clip.open(AudioSystem.getAudioInputStream(bis));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bis.close();
            audioPlayer.getAudioClip().getInputStream().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return clip;
    }

    @Override
    public void unloadAudio(AudioPlayer audioPlayer) {
        ((Clip) audioPlayer.getClipRef()).close();
        audioPlayer.setClipRef(null);
    }

    @Override
    public void play(AudioPlayer audioPlayer) {
        ((Clip) audioPlayer.getClipRef()).start();
    }

    @Override
    public void loop(AudioPlayer audioPlayer) {
        ((Clip) audioPlayer.getClipRef()).loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void pause(AudioPlayer audioPlayer) {
        ((Clip) audioPlayer.getClipRef()).stop();
    }

    @Override
    public double getLength(AudioPlayer audioPlayer) {
        return ((Clip) audioPlayer.getClipRef()).getMicrosecondLength() / 1000.0;
    }

    @Override
    public double getPosition(AudioPlayer audioPlayer) {
        return ((Clip) audioPlayer.getClipRef()).getMicrosecondPosition() / 1000.0;
    }

    @Override
    public void setPosition(AudioPlayer audioPlayer, double position) {
        ((Clip) audioPlayer.getClipRef()).setMicrosecondPosition((long) (position * 1000));
    }

    @Override
    public void setGain(AudioPlayer audioPlayer, double gain) {
        FloatControl gainControl = (FloatControl) ((Clip) audioPlayer.getClipRef()).getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(gain));
    }

    @Override
    public void setPan(AudioPlayer audioPlayer, double pan) {
        FloatControl panControl = (FloatControl) ((Clip) audioPlayer.getClipRef()).getControl(FloatControl.Type.PAN);
        panControl.setValue((float) pan);
    }

    @Override
    public boolean isPlaying(AudioPlayer audioPlayer) {
        return false;
    }
}