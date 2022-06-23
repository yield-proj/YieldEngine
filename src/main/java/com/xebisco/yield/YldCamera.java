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

public class YldCamera
{
    private Vector2 position = new Vector2();

    public YldCamera()
    {

    }

    public YldCamera(Vector2 position)
    {
        this.position = position;
    }

    public YldCamera get() {
        return new YldCamera(position.get());
    }

    @Deprecated
    public static YldCamera clone(YldCamera camera)
    {
        final YldCamera cam = new YldCamera();
        cam.position = camera.position;
        return cam;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }
}
