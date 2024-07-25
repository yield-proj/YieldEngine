/*
 * Copyright [2022-2024] [Xebisco]
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

package com.xebisco.yieldengine.core.render;

import com.xebisco.yieldengine.core.Transform;
import com.xebisco.yieldengine.core.camera.ICamera;

import java.io.Serializable;

public final class DrawInstruction implements Serializable {
    private static final long serialVersionUID = -7313521463068476657L;
    private String type;
    private Transform transform;
    private Serializable[] drawObjects;
    private ICamera camera;

    public enum DrawInstructionType {
        DRAW_IMAGE("dw_img"), // {COLOR4, SIZE, IMAGE}
        DRAW_RECTANGLE("dw_rect"), // {COLOR4, SIZE}
        DRAW_LINE("dw_ln"), // {COLOR4, POINT_1, POINT_2, THICKNESS(FLOAT)}
        DRAW_ELLIPSE("dw_ellipse"), // {COLOR4, SIZE}
        DRAW_TEXT("dw_text"), // {COLOR4, FONT, STRING}
        CLEAR_SCREEN("clr"); //{COLOR3}
        private final String typeString;

        DrawInstructionType(String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return typeString;
        }
    }

    public String getType() {
        return type;
    }

    public DrawInstruction setType(String type) {
        this.type = type;
        return this;
    }

    public Transform getTransform() {
        return transform;
    }

    public DrawInstruction setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    public Object[] getDrawObjects() {
        return drawObjects;
    }

    public DrawInstruction setDrawObjects(Serializable[] drawObjects) {
        this.drawObjects = drawObjects;
        return this;
    }

    public ICamera getCamera() {
        return camera;
    }

    public DrawInstruction setCamera(ICamera camera) {
        this.camera = camera;
        return this;
    }
}
