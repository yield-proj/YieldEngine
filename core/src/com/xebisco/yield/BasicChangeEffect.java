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
public class BasicChangeEffect {//TODO implement these effects into the Application class.

    /**
     * The FadeIn class is a subclass of ChangeSceneEffect that renders a black rectangle that fades in over a specified
     * amount of time.
     */
    public static class FadeIn extends ChangeSceneEffect {

        private final DrawInstruction di = new DrawInstruction();

        protected FadeIn(double timeToWait) {
            super(timeToWait);
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
            di.setSize(getSceneResolution());
        }

        @Override
        public void render(PlatformGraphics graphics) {
            di.getInnerColor().setAlpha(getPassedTime() / getTimeToWait());
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

        protected FadeOut(double timeAfter) {
            super(0);
            this.timeAfter = timeAfter;
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
            di.setSize(getSceneResolution());
        }

        @Override
        public void render(PlatformGraphics graphics) {
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

        protected FadeInAndOut(double timeToWait, double timeAfter) {
            super(timeToWait);
            this.timeAfter = timeAfter;
            di.setType(DrawInstruction.Type.RECTANGLE);
            di.setFilled(true);
            di.setInnerColor(new Color(Colors.BLACK));
            di.setPosition(new Vector2D());
            di.setSize(getSceneResolution());
        }

        @Override
        public void render(PlatformGraphics graphics) {
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
}
