package com.xebisco.yieldengine.alimpl;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundManager {
    private static long device, context;
    private static AtomicBoolean running = new AtomicBoolean();
    private static List<Runnable> runOnAudioThread = new ArrayList<>();

    public static void initAL() {
        device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);
    }

    public static long getDevice() {
        return device;
    }

    public static void setDevice(long device) {
        SoundManager.device = device;
    }

    public static long getContext() {
        return context;
    }

    public static void setContext(long context) {
        SoundManager.context = context;
    }

    public static AtomicBoolean getRunning() {
        return running;
    }

    public static void setRunning(AtomicBoolean running) {
        SoundManager.running = running;
    }

    public static List<Runnable> getRunOnAudioThread() {
        return runOnAudioThread;
    }

    public static void setRunOnAudioThread(List<Runnable> runOnAudioThread) {
        SoundManager.runOnAudioThread = runOnAudioThread;
    }
}
