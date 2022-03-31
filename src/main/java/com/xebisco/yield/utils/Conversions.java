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

package com.xebisco.yield.utils;

import com.xebisco.yield.Color;
import com.xebisco.yield.Vector2;
import org.newdawn.slick.geom.Vector2f;

public final class Conversions
{
    public static java.awt.Color toAWTColor(final Color color)
    {
        return new java.awt.Color((int) (color.getR() * 255), (int) (color.getG() * 255), (int) (color.getB() * 255), (int) (color.getA() * 255));
    }

    public static Color toColor(final java.awt.Color color)
    {
        return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public static Color toColor(final org.newdawn.slick.Color color)
    {
        return new Color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public static org.newdawn.slick.Color toSlickColor(final Color color)
    {
        return new org.newdawn.slick.Color(color.getR(), color.getG(), color.getB(), color.getA());
    }

    public static Vector2 toVector2(final Vector2f vector2f)
    {
        return new Vector2(vector2f.x, vector2f.y);
    }

    public static Vector2f toSlickVector2f(final Vector2 vector2)
    {
        return new Vector2f(vector2.x, vector2.y);
    }

}
