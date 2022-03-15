/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield.components;

import com.xebisco.yield.AudioClip;
import com.xebisco.yield.Component;

import javax.sound.sampled.*;
import java.io.IOException;

public class AudioPlayer extends Component
{
    private AudioClip audioClip;
    private Clip clip;
    private AudioInputStream audioInputStream;
    private long position;
    private boolean loop;

    public AudioPlayer()
    {
        try
        {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    public void load(AudioClip audioClip)
    {
        try
        {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioClip.getUrl());
            clip.open(inputStream);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    public float getVolume()
    {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     * @param volume 0.0f to 1.0f
     */
    public void setVolume(float volume)
    {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public void play()
    {
        restart();
        resume();
    }

    public void setFramePosition(int frame)
    {
        position = frame;
        clip.setFramePosition(frame);
    }

    public void setMicrosecondPosition(long pos)
    {
        position = pos;
        clip.setMicrosecondPosition(pos);
    }

    public void pause()
    {
        position = clip.getFramePosition();
        clip.stop();
    }

    public void resume()
    {
        clip.setFramePosition((int) position);
        clip.start();
    }

    public void restart()
    {
        position = audioClip.getStartPos().getPosition();
        if (audioClip.getStartPos().isMicrosecond())
            clip.setMicrosecondPosition(0);
        else
            clip.setFramePosition(0);
    }

    @Override
    public void onDestroy()
    {
        if (clip != null)
        {
            setLoop(false);
            pause();
            clip.close();
        }
    }

    public boolean isLoop()
    {
        return loop;
    }

    public void setLoop(boolean loop)
    {
        this.loop = loop;
        if (loop)
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        else
        {
            clip.loop(0);
        }
    }

    public void setLoop(int count)
    {
        if (count < 0)
            throw new IllegalArgumentException("count can't be less than 0");
        loop = count != 0;
        clip.loop(count);
    }

    public Clip getClip()
    {
        return clip;
    }

    public void setClip(Clip clip)
    {
        this.clip = clip;
    }

    public AudioInputStream getAudioInputStream()
    {
        return audioInputStream;
    }

    public void setAudioInputStream(AudioInputStream audioInputStream)
    {
        this.audioInputStream = audioInputStream;
    }

    public long getPosition()
    {
        return position;
    }

    public AudioClip getAudioClip()
    {
        return audioClip;
    }

    public void setAudioClip(AudioClip audioClip)
    {
        this.audioClip = audioClip;
    }
}
