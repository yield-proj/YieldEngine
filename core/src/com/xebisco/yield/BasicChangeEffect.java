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

/**
 * The BasicChangeEffect class contains three subclasses that implement fade-in, fade-out, and fade-in-and-out effects for
 * changing scenes in an application.
 */
public class BasicChangeEffect {

    /**
     * The FadeIn class is a subclass of ChangeSceneEffect that renders a black rectangle that fades in over a specified
     * amount of time.
     */
    public static class FadeIn extends ChangeSceneEffect {

        private final DrawInstruction di = new DrawInstruction();

        public FadeIn(double timeToWait, boolean stopUpdatingScene) {
            super(timeToWait, stopUpdatingScene);
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
        }

        @Override
        public void render(PlatformGraphics graphics) {
            di.setSize(getApplication().getResolution());
            di.getInnerColor().setAlpha(getPassedTime() / getTimeToWait());
            System.out.println(di.getSize());
            if (di.getInnerColor().getAlpha() == 1) {
                setFinished(true);
            }
            graphics.draw(di);
        }
    }

    /**
     * The FadeOut class is a subclass of ChangeSceneEffect that renders a black rectangle that fades out over a specified
     * time period.
     */
    public static class FadeOut extends ChangeSceneEffect {

        private final DrawInstruction di = new DrawInstruction();
        private final double timeAfter;

        public FadeOut(double timeAfter, boolean stopUpdatingScene) {
            super(0, stopUpdatingScene);
            this.timeAfter = timeAfter;
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
        }

        @Override
        public void render(PlatformGraphics graphics) {
            di.setSize(getApplication().getResolution());
            di.getInnerColor().setAlpha(1 - getPassedTime() / timeAfter);
            if (di.getInnerColor().getAlpha() == 0) {
                setFinished(true);
            }
            graphics.draw(di);
        }
    }

    /**
     * The FadeInAndOut class is a subclass of ChangeSceneEffect that creates a fade-in and fade-out effect by rendering a
     * black rectangle with changing alpha values.
     */
    public static class FadeInAndOut extends ChangeSceneEffect {

        private final DrawInstruction di = new DrawInstruction();
        private final double timeAfter;

        public FadeInAndOut(double timeToWait, double timeAfter, boolean stopUpdatingScene) {
            super(timeToWait, stopUpdatingScene);
            this.timeAfter = timeAfter;
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
        }

        @Override
        public void render(PlatformGraphics graphics) {
            di.setSize(getApplication().getResolution());
            double a = getPassedTime() / timeAfter;
            if (a > 1) {
                di.getInnerColor().setAlpha(1 - (getPassedTime() - getTimeToWait()) / timeAfter);
                if (di.getInnerColor().getAlpha() == 0) {
                    setFinished(true);
                }
            } else {
                di.getInnerColor().setAlpha(getPassedTime() / getTimeToWait());
            }
            graphics.draw(di);
        }
    }

    public static class SlideUp extends ChangeSceneEffect {
        private Texture sliderUp, sliderDown;
        private final DrawInstruction di = new DrawInstruction();

        public SlideUp(double slideTime, boolean stopUpdatingScene) {
            super(slideTime / 2, stopUpdatingScene);
        }

        @Override
        public void render(PlatformGraphics graphics) {
            if (sliderUp == null)
                sliderUp = new Texture("slideSceneChangeEffect.png", getApplication().getTextureManager());
            if (sliderDown == null)
                sliderDown = new Texture("invertedSlideSceneChangeEffect.png", getApplication().getTextureManager());
            if (getPassedTime() >= getTimeToWait() * 2.0)
                setFinished(true);
            double y = getPassedTime() / getTimeToWait() * (getApplication().getResolution().getHeight() + sliderUp.getSize().getHeight() + sliderDown.getSize().getHeight()) - getApplication().getResolution().getHeight() - sliderUp.getSize().getHeight();
            di.setSize(getApplication().getResolution());
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D(0, y));
            graphics.draw(di);
            di.setSize(sliderUp.getSize());
            di.setRenderRef(sliderUp.getImageRef());
            di.setType(DrawInstruction.Type.IMAGE);
            di.setPosition(new Vector2D(0, y + getApplication().getResolution().getHeight() / 2 + di.getSize().getHeight() / 2));
            graphics.draw(di);
            di.setSize(sliderDown.getSize());
            di.setRenderRef(sliderDown.getImageRef());
            di.setType(DrawInstruction.Type.IMAGE);
            di.setPosition(new Vector2D(0, y - getApplication().getResolution().getHeight() / 2  - di.getSize().getHeight() / 2));
            graphics.draw(di);
        }
    }

    public static class SlideDown extends ChangeSceneEffect {
        private Texture sliderUp, sliderDown;
        private final DrawInstruction di = new DrawInstruction();

        public SlideDown(double slideTime, boolean stopUpdatingScene) {
            super(slideTime / 2, stopUpdatingScene);
        }

        @Override
        public void render(PlatformGraphics graphics) {
            if (sliderUp == null)
                sliderUp = new Texture("slideSceneChangeEffect.png", getApplication().getTextureManager());
            if (sliderDown == null)
                sliderDown = new Texture("invertedSlideSceneChangeEffect.png", getApplication().getTextureManager());
            if (getPassedTime() >= getTimeToWait() * 2.0)
                setFinished(true);
            double y = getApplication().getResolution().getHeight() + sliderDown.getSize().getHeight() - getPassedTime() / getTimeToWait() * (getApplication().getResolution().getHeight() + sliderUp.getSize().getHeight() + sliderDown.getSize().getHeight());
            di.setSize(getApplication().getResolution());
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D(0, y));
            graphics.draw(di);
            di.setSize(sliderUp.getSize());
            di.setRenderRef(sliderUp.getImageRef());
            di.setType(DrawInstruction.Type.IMAGE);
            di.setPosition(new Vector2D(0, y + getApplication().getResolution().getHeight() / 2 + di.getSize().getHeight() / 2));
            graphics.draw(di);
            di.setSize(sliderDown.getSize());
            di.setRenderRef(sliderDown.getImageRef());
            di.setType(DrawInstruction.Type.IMAGE);
            di.setPosition(new Vector2D(0, y - getApplication().getResolution().getHeight() / 2  - di.getSize().getHeight() / 2));
            graphics.draw(di);
        }
    }
}
