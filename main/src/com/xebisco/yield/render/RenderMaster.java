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
import java.util.TreeSet;


/**
 * This is an interface to handle rendering, audio and files.
 *
 * @author Xebisco
 * @since 4-1.2
 */
public interface RenderMaster extends VisualUtils, AudioUtils, FileUtils {

    /**
     * @param renderables A TreeSet of Renderable objects.
     */
    void start(Set<Renderable> renderables);

    /**
     * It creates a window
     *
     * @param configuration The configuration of the window.
     * @return A SampleWindow object.
     */
    SampleWindow initWindow(WindowConfiguration configuration);

    /**
     * This function is called at the end of each frame, to the RenderMaster render the game. The `view`
     * parameter is a `View` object that you can use to get information about the current view
     *
     * @param backgroundColor The color of the background.
     */
    void frameEnd(Color backgroundColor, int width, int height, int offsetX, int offsetY, float scaleX, float scaleY);

    /**
     * This function is called when the game is resized.
     * The first parameter is the new width of the game. The second parameter is the new height of the game
     *
     * @param width  The width of the game
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

    /**
     * Sets the thread related task to be executed.
     *
     * @param threadTask The thread related task to be executed.
     */
    void setThreadTask(YldTask threadTask);

    /**
     * Returns the width of the given string in pixels, using the given font.
     *
     * @param str The string to get the width of.
     * @param font The font to use.
     * @return The width of the string in pixels.
     */
    float getStringWidth(String str, String font);

    /**
     * Returns the height of the given string in pixels, using the given font
     *
     * @param str The string to get the height of
     * @param font The font to use.
     * @return The height of the string in pixels.
     */
    float getStringHeight(String str, String font);
}
