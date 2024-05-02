/*
 * Copyright [2022-2024] [Xebisco]
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
 * Provides an abstract implementation of the {@link Behavior} interface,
 * tracking the number of frames since its activation and defining the lifecycle of a behavior.
 */
public abstract class AbstractBehavior implements Behavior {
    private long frames;

    /**
     * Updates the behavior each frame. It calls {@code onStart()} on the first frame,
     * then calls {@code onUpdate(ContextTime)} on every frame including the first.
     *
     * @param time The context time for the current frame, providing timing information.
     */
    public void tick(ContextTime time) {
        if(frames == 0) {
            onStart(); // Called only on the first frame.
        }
        onUpdate(time); // Called every frame.
        frames++; // Increment the frame count.
    }

    /**
     * Returns the number of frames since the behavior started.
     *
     * @return The number of frames since the behavior was activated.
     */
    public long frames() {
        return frames;
    }
}
