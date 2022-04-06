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

import org.newdawn.slick.TrueTypeFont;
import java.awt.*;

public class Text extends Shape
{
    private String contents = "Sample Text";
    private static Font defaultFont = new Font("arial", Font.PLAIN, 30);

    public Text()
    {
    }

    public Text(String contents)
    {
        this.contents = contents;
    }

    @Override
    public void parameters(YldGraphics graphics)
    {

    }

    @Override
    public void previous(YldGraphics graphics)
    {
        setFont(new Font(defaultFont.getFontName(), defaultFont.getStyle(), defaultFont.getSize()));
        getObj().type = Obj.ShapeType.TEXT;
    }

    @Override
    public void process(Obj obj)
    {
        obj.x = (int) (getEntity().getTransform().position.x);
        obj.y = (int) (getEntity().getTransform().position.y);
        obj.value = contents;
    }

    public String getContents()
    {
        return contents;
    }

    public void setContents(String contents)
    {
        this.contents = contents;
    }

    public Font getFont()
    {
        return getObj().font;
    }

    public void setFont(Font font)
    {
        getObj().font = font;
        getObj().updateSlickFont();
    }

    public static Font getDefaultFont()
    {
        return defaultFont;
    }

    public static void setDefaultFont(Font defaultFont)
    {
        Text.defaultFont = defaultFont;
    }
}
