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

/**
 * It's a class that plays audio clips.
 */
public class AudioPlayer extends Component {
    private AudioClip audioClip;
    private int playerID;
    private static int players;
    private boolean loop;

    public AudioPlayer() {
        playerID = players;
        players++;
    }

    /**
     * It loads an audio clip into the game.
     *
     * @param audioClip The AudioClip object that you want to load.
     * @param multiThread This is the MultiThread object that will be used to load the audio clip.
     */
    public void load(AudioClip audioClip, MultiThread multiThread) {
        game.getHandler().getRenderMaster().loadAudioClip(audioClip, this, multiThread, this);
    }
    /**
     * Get the volume of the audio player.
     *
     * @return The volume of the audio player.
     */
    public float getVolume() {
        return game.getHandler().getRenderMaster().getVolume(this);
    }

    /**
     * Set the volume of the audio player to the given value.
     *
     * @param volume The volume to set the audio player to.
     */
    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume is not valid: " + volume);
        game.getHandler().getRenderMaster().setVolume(this, volume);
    }

    /**
     *  Plays the audio player.
     */
    public void play() {
        restart();
        resume();
    }

    /**
     * Sets an audioPlayer position in microseconds.
     *
     * @param pos The position in microseconds to set the position to.
     */
    public void setMicrosecondPosition(long pos) {
        game.getHandler().getRenderMaster().setMicrosecondPosition(this, pos);
    }

    /**
     * It returns the current position of an audio player in microseconds.
     *
     * @return The current position of the audio player in microseconds.
     */
    public long getMicrosecondPosition() {
        return game.getHandler().getRenderMaster().getMicrosecondPosition(this);
    }

    /**
     * Pause the audio player.
     */
    public void pause() {
        game.getHandler().getRenderMaster().pausePlayer(this);
    }

    /**
     * Resumes the audio player
     */
    public void resume() {
        game.getHandler().getRenderMaster().resumePlayer(this);
    }

    /**
     * Returns true if the player is running, false otherwise
     *
     * @return A boolean value.
     */
    public boolean isRunning() {
        return game.getHandler().getRenderMaster().isPlayerRunning(this);
    }

    /**
     * This function sets the current position of the player to 0.
     */
    public void restart() {
        setMicrosecondPosition(0);
    }

    @Override
    public void onDestroy() {
        game.getHandler().getRenderMaster().flushPlayer(this);
    }

    /**
     * Returns true if the audio is looping, false otherwise
     *
     * @return The boolean value of the loop variable.
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Sets whether the audio player should loop or not
     *
     * @param loop true or false
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
        game.getHandler().getRenderMaster().setLoop(this, loop);
    }

    /**
     * Sets the number of times the audio should loop
     *
     * @param count The number of times the audio should loop.
     */
    public void setLoop(int count) {
        if (count < 0)
            throw new IllegalArgumentException("count can't be less than 0");
        loop = count != 0;
        game.getHandler().getRenderMaster().setLoop(this, count);
    }

    /**
     * This function returns the audio clip.
     *
     * @return The audioClip variable is being returned.
     */
    public AudioClip getAudioClip() {
        return audioClip;
    }

    /**
     * Sets the audio clip.
     *
     * @param audioClip The audio clip value to set.
     */
    public void setAudioClip(AudioClip audioClip) {
        setAudioClip(audioClip, null);
    }

    /**
     * This function sets the audio clip and loads it.
     *
     * @param audioClip The AudioClip to load.
     * @param multiThread This is a boolean value that determines whether the audio clip will be loaded in a separate
     * thread.
     */
    public void setAudioClip(AudioClip audioClip, MultiThread multiThread) {
        this.audioClip = audioClip;
        load(audioClip, multiThread);
    }

    /**
     * This function returns the playerID of the audioPlayer
     *
     * @return The playerID
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * This function sets the playerID variable to the value of the playerID parameter.
     *
     * @param playerID The ID of the player.
     */
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
