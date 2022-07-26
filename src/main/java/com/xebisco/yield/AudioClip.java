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

package com.xebisco.yield;

/**
 * A instance of an audio file, an implementation of RelativePath.
 */
public class AudioClip extends RelativeFile {

    private AudioClipPos startPos = new AudioClipPos();
    private String cache = "";

    /**
     * Creates a AudioClip from the given relative path.
     * @param relativePath The relative path of the audio clip.
     */
    public AudioClip(String relativePath) {
        super(relativePath);
    }

    /**
     * Getter of the startPos of this AudioClip.
     *
     * @return The startPos variable.
     */
    public AudioClipPos getStartPos() {
        return startPos;
    }

    /**
     * Setter of the startPos of this AudioClip.
     */
    public void setStartPos(AudioClipPos startPos) {
        this.startPos = startPos;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
