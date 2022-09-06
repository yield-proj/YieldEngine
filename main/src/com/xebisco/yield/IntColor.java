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

/**
 * It's a Color class that takes integer values for the red, green, blue, and alpha components
 */
public class IntColor extends Color
{
    public IntColor(int r, int g, int b)
    {
        super(r / 255f, g / 255f, b / 255f);
    }

    public IntColor(int r, int g, int b, int a)
    {
        super(r / 255f, g / 255f, b / 255f, a / 255f);
    }
}
