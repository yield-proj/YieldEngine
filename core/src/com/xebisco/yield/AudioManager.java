package com.xebisco.yield;

public interface AudioManager {
    Object loadAudio(Audio audio);

    void unloadAudio(Audio audio);

    Object loadAudioPlayer(AudioPlayer audioPlayer);

    void unloadAudioPlayer(AudioPlayer audioPlayer);

    void play(AudioPlayer audioPlayer);

    void pause(AudioPlayer audioPlayer);

    double getLength(AudioPlayer audioPlayer);

    double getPosition(AudioPlayer audioPlayer);

    void setPosition(AudioPlayer audioPlayer, double position);

    double getGain(AudioPlayer audioPlayer);

    void setGain(AudioPlayer audioPlayer, double gain);

    double getPan(AudioPlayer audioPlayer);

    void setPan(AudioPlayer audioPlayer, double pan);
}

