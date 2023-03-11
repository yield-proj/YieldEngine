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

import java.io.InputStream;

public class Audio extends FileInput {
    private final AudioManager audioManager;
    private Object clipRef;
    public Audio(String relativePath, AudioManager audioManager) {
        super(relativePath);
        this.audioManager = audioManager;
        clipRef = audioManager.loadAudio(this);
    }

    public Audio(InputStream inputStream, AudioManager audioManager) {
        super(inputStream);
        this.audioManager = audioManager;
        clipRef = audioManager.loadAudio(this);
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public Object getClipRef() {
        return clipRef;
    }

    public void setClipRef(Object clipRef) {
        this.clipRef = clipRef;
    }
}
