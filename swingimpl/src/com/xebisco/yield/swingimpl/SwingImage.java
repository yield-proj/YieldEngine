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

package com.xebisco.yield.swingimpl;

import com.xebisco.yield.Color;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.util.concurrent.CompletableFuture;

public class SwingImage {
    private final BufferedImage originalImage;
    private Image filteredImage;

    private Color lastColor;

    public void updateImage(Color color) {
        if(filteredImage == null)
            filterImage(color);
        else if(lastColor.hashCode() != color.hashCode() && !lastColor.equals(color))
            CompletableFuture.runAsync(() -> filterImage(color));
        this.lastColor = color;
    }

    private void filterImage(Color color) {
        filteredImage = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(originalImage.getSource(), new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                com.xebisco.yield.Color img = new com.xebisco.yield.Color(rgb);
                return new com.xebisco.yield.Color(img.getRed() * color.getRed(), img.getGreen() * color.getGreen(), img.getBlue() * color.getBlue(), img.getAlpha() * color.getAlpha()).getARGB();
            }
        }));
    }

    public SwingImage(BufferedImage originalImage) {
        this.originalImage = originalImage;
    }

    public BufferedImage originalImage() {
        return originalImage;
    }

    public Image filteredImage() {
        return filteredImage;
    }

    public SwingImage setFilteredImage(Image filteredImage) {
        this.filteredImage = filteredImage;
        return this;
    }
}
