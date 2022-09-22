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

import com.xebisco.yield.*;
import com.xebisco.yield.config.WindowConfiguration;

import java.util.Set;


/**
 * This is an interface to handle rendering, audio and files.
 *
 * @author Xebisco
 * @since 4-1.2
 */
public interface RenderMaster extends VisualUtils, AudioUtils, FileUtils {
    /**
     * This function returns a SampleGraphics object.
     *
     * @return A SampleGraphics object.
     */
    SampleGraphics initGraphics();

    /**
     * Returns a SampleGraphics object that contains the graphics for this sample.
     *
     * @return The specificGraphics method is being returned.
     */
    SampleGraphics specificGraphics();

    /**
     * This function is called before the game starts
     *
     * @param game The game object.
     */
    void before(YldGame game);

    /**
     * It creates a window
     *
     * @param configuration The configuration of the window.
     * @return A SampleWindow object.
     */
    SampleWindow initWindow(WindowConfiguration configuration);

    /**
     * "This function is called once per frame, before the frame is rendered."
     * The `graphics` parameter is a `SampleGraphics` object that you can use to draw things on the screen.
     *
     * @param graphics The graphics object that you can use to draw to the screen.
     */
    void frameStart(SampleGraphics graphics);

    /**
     * This function is called at the end of each frame, to the RenderMaster render the game. The `view`
     * parameter is a `View` object that you can use to get information about the current view
     *
     * @param view The view object.
     */
    void frameEnd(View view);

    /**
     * This function is called when the game is resized.
     * The first parameter is the new width of the game. The second parameter is the new height of the game
     *
     * @param width The width of the game
     * @param height The height of the game
     */
    void onResize(int width, int height);

    /**
     * Returns true if the game can start.
     *
     * @return A boolean value.
     */
    boolean canStart();

    /**
     * It returns the current FPS count.
     *
     * @return the number of frames per second.
     */
    float fpsCount();

    /**
     * Return a set of integers representing the keys that are currently being pressed.
     *
     * @return A set of integers.
     */
    Set<Integer> pressing();

    /**
     * Returns the current x-coordinate of the mouse.
     *
     * @return The x coordinate of the mouse.
     */
    int mouseX();

    /**
     * Returns the y-coordinate of the mouse
     *
     * @return The mouseY() function returns the y-coordinate of the mouse.
     */
    int mouseY();

    void close();
}
