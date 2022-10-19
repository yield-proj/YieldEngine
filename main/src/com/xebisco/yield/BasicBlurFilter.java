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

public class BasicBlurFilter implements YldFilter {

    @Override
    public void process(Pixel pixel) {
        pixel.setOutColor(pixel.getColor().get());
        pixel.setOutLocation(pixel.getLocation().get());
        if (pixel.getLocation().x % 2 == 0) {
            Pixel rd = pixel.getPixelGrid().pixelFromIndex((int) pixel.getLocation().x + 1, (int) pixel.getLocation().y + 1), ld = pixel.getPixelGrid().pixelFromIndex((int) pixel.getLocation().x - 1, (int) pixel.getLocation().y + 1),
                    ru = pixel.getPixelGrid().pixelFromIndex((int) pixel.getLocation().x + 1, (int) pixel.getLocation().y - 1), lu = pixel.getPixelGrid().pixelFromIndex((int) pixel.getLocation().x - 1, (int) pixel.getLocation().y - 1);
            pixel.getOutColor().setR((rd.getColor().getR() + ru.getColor().getR() + ld.getColor().getR() + ru.getColor().getR()) / 4f);
            pixel.getOutColor().setG((rd.getColor().getG() + ru.getColor().getG() + ld.getColor().getG() + ru.getColor().getG()) / 4f);
            pixel.getOutColor().setB((rd.getColor().getB() + ru.getColor().getB() + ld.getColor().getB() + ru.getColor().getB()) / 4f);
        }

    }
}
