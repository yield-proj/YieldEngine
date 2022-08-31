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

package com.xebisco.yield;

import javax.sound.sampled.*;

public class AudioPlayer extends Component {
    private AudioClip audioClip;
    private int playerID;
    private static int players;
    private boolean loop;

    public AudioPlayer() {
        playerID = players;
        players++;
    }

    public void load(AudioClip audioClip, MultiThread multiThread) {
        game.getHandler().getRenderMaster().loadAudioClip(audioClip, this, multiThread, this);
    }

    public float getVolume() {
        return game.getHandler().getRenderMaster().getVolume(this);
    }

    /**
     * @param volume 0.0f to 1.0f
     */
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume is not valid: " + volume);
        game.getHandler().getRenderMaster().setVolume(this, volume);
    }

    public void play() {
        restart();
        resume();
    }

    public void setMicrosecondPosition(long pos) {
        game.getHandler().getRenderMaster().setMicrosecondPosition(this, pos);
    }

    public long getMicrosecondPosition() {
        return game.getHandler().getRenderMaster().getMicrosecondPosition(this);
    }

    public void pause() {
        game.getHandler().getRenderMaster().pausePlayer(this);
    }

    public void resume() {
        game.getHandler().getRenderMaster().resumePlayer(this);
    }

    public boolean isRunning() {
        return game.getHandler().getRenderMaster().isPlayerRunning(this);
    }

    public void restart() {
        setMicrosecondPosition(0);
    }

    @Override
    public void onDestroy() {
        game.getHandler().getRenderMaster().flushPlayer(this);
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        game.getHandler().getRenderMaster().setLoop(this, loop);
    }

    public void setLoop(int count) {
        if (count < 0)
            throw new IllegalArgumentException("count can't be less than 0");
        loop = count != 0;
        game.getHandler().getRenderMaster().setLoop(this, count);
    }

    public AudioClip getAudioClip() {
        return audioClip;
    }

    public void setAudioClip(AudioClip audioClip) {
        setAudioClip(audioClip, null);
    }

    public void setAudioClip(AudioClip audioClip, MultiThread multiThread) {
        this.audioClip = audioClip;
        load(audioClip, multiThread);
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public static int getPlayers() {
        return players;
    }

    public static void setPlayers(int players) {
        AudioPlayer.players = players;
    }

    @Override
    public void setGame(YldGame game) {
        if(this.game == null) {
            game.getHandler().getRenderMaster().loadAudioPlayer(this);
        }
        super.setGame(game);
    }
}
