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
@FunctionalInterface
public interface PixelProcessor {
    /**
     * The function "process" takes a Pixel object as input and returns a Pixel object as output.
     *
     * @param pixel The "pixel" parameter is likely an object or data structure that represents a single pixel in an image.
     * It may contain information such as the pixel's color, position, and any other relevant attributes. The "Pixel
     * process" function likely takes this pixel object as input and performs some kind of processing or
     * @return It is not specified what is being returned by the function. The function name and parameter suggest that it
     * may be processing a single pixel and returning a modified version of it, but without more information it is
     * impossible to determine the exact return type and behavior of the function.
     */
    Pixel process(Pixel pixel);
}
