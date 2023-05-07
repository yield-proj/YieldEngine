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

package com.xebisco.yield.ui;

import com.xebisco.yield.*;

import java.util.concurrent.CompletableFuture;

/**
 * The InputUI class is responsible for displaying a message and visual feedback when a controller is connected or
 * disconnected in a Yield application.
 */
public class InputUI extends ComponentBehavior {
    private final DrawInstruction instruction = new DrawInstruction();
    private int stage = -1, lastControllerNum;
    private String msg;
    private double f, target = 150, onMessage;
    private Font font;
    private AudioPlayer audioPlayer;

    @Override
    public void onStart() {
        if (getApplication().getControllerManager() != null) {
            lastControllerNum = getApplication().getControllerManager().getNumControllers();
            font = new Font("Pixeboy.ttf", 40, getApplication().getFontLoader());
            audioPlayer = getComponent(AudioPlayer.class);
        } else {
            getEntity().dispose();
        }
    }

    @Override
    public void onUpdate() {
        if (getApplication().getControllerManager() != null) {
            if (getApplication().getControllerManager().getNumControllers() > lastControllerNum) {
                stage = 0;
                msg = "Controller Connected";
                CompletableFuture.runAsync(() -> {
                    audioPlayer.setAudioClip(new FileInput("ioconnected.wav"));
                    audioPlayer.setGain(.1);
                    audioPlayer.play();
                });
            }
            if (getApplication().getControllerManager().getNumControllers() < lastControllerNum) {
                stage = 0;
                msg = "Controller Disconnected";
                CompletableFuture.runAsync(() -> {
                    audioPlayer.setAudioClip(new FileInput("iodisconnected.wav"));
                    audioPlayer.setGain(.1);
                    audioPlayer.play();
                });
            }
            if (stage == 0) {
                onMessage += getTime().getDeltaTime();
                f += (target - f) / 10.0;
                if (onMessage > 3) {
                    onMessage = 0;
                    stage = 1;
                }
            }

            if (stage == 1) {
                f -= (target - f) / 10.0;
                if (f < 0.01) {
                    stage = -1;
                }
            }
            lastControllerNum = getApplication().getControllerManager().getNumControllers();
        }
    }

    @Override
    public void render(PlatformGraphics graphics) {
        if (getApplication().getControllerManager() != null) {
            if (stage == -1)
                return;
            instruction.setType(DrawInstruction.Type.IMAGE);
            for (int i = 0; i < 4; i++) {
                if (getApplication().getControllerManager().getControllerIndex(i).isConnected())
                    instruction.setRenderRef(getApplication().getControllerTexture().getImageRef());
                else instruction.setRenderRef(getApplication().getTranslucentControllerTexture().getImageRef());
                instruction.setSize(new Size2D(50, 50));
                instruction.setPosition(new Vector2D(getApplication().getScene().getCamera().getX() - getApplication().getViewportSize().getWidth() / 2.0 + 50 + getApplication().getControllerTexture().getSize().getWidth() / 2.0 * i - (target - f), -getApplication().getViewportSize().getHeight() / 2.0 + 50 + getApplication().getScene().getCamera().getY()));
                graphics.draw(instruction);
            }
            target = getApplication().getFontLoader().getStringWidth(msg, font.getFontRef()) + 80;
            instruction.setType(DrawInstruction.Type.RECTANGLE);
            instruction.setInnerColor(Colors.DARK_PURPLE.darker());
            instruction.setBorderColor(Colors.PURPLE);
            instruction.setBorderThickness(5);
            instruction.setFilled(true);
            instruction.setSize(new Size2D(f, 50));
            instruction.setPosition(new Vector2D(getApplication().getScene().getCamera().getX(), -getApplication().getViewportSize().getHeight() / 2f + 50 + getApplication().getScene().getCamera().getY()));
            graphics.draw(instruction);
            if (f > 50) {
                instruction.setType(DrawInstruction.Type.IMAGE);
                instruction.setRenderRef(getApplication().getControllerTexture().getImageRef());
                instruction.setSize(new Size2D(50, 50));
                instruction.setPosition(new Vector2D(getApplication().getScene().getCamera().getX() - f / 2.0 + 30, -getApplication().getViewportSize().getHeight() / 2.0 + 50 + getApplication().getScene().getCamera().getY()));
                graphics.draw(instruction);
            }
            if (f > target - 30) {
                instruction.setType(DrawInstruction.Type.TEXT);
                instruction.setRenderRef(msg);
                instruction.setInnerColor(new Color(Colors.WHITE));
                instruction.getInnerColor().setAlpha(1 - (target - f) / 30.0);
                instruction.setFont(font);
                instruction.getSize().set(getApplication().getFontLoader().getStringWidth(msg, font.getFontRef()), getApplication().getFontLoader().getStringHeight(msg, font.getFontRef()));
                instruction.setPosition(new Vector2D(getApplication().getScene().getCamera().getX() + 30, -getApplication().getViewportSize().getHeight() / 2f + 48 + getApplication().getScene().getCamera().getY()));
                graphics.draw(instruction);
            }
        }
    }
}