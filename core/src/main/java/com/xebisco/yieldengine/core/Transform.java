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

package com.xebisco.yieldengine.core;

import com.xebisco.yieldengine.annotations.Editable;
import com.xebisco.yieldengine.annotations.TransformMatrix;
import com.xebisco.yieldengine.annotations.Visible;
import org.joml.*;

import java.io.Serializable;

public class Transform implements Serializable {
    private static final long serialVersionUID = 2642369926201568158L;
    @Visible
    @Editable
    @TransformMatrix
    private final Matrix4f transformMatrix = new Matrix4f();

    public Transform(Transform transform) {
        transform.transformMatrix.get(transformMatrix);
    }

    public Transform() {
        transformMatrix.scale(new Vector3f(1, 1, 1));
    }

    public Transform translate(Vector3fc translation) {
        transformMatrix.translateLocal(translation);
        return this;
    }

    public Transform translate(float x, float y, float z) {
        translate(new Vector3f(x, y, z));
        return this;
    }

    public Transform translate(float x, float y) {
        translate(new Vector3f(x, y, 0));
        return this;
    }

    public Transform translate(Vector2fc translation) {
        translate(translation.x(), translation.y());
        return this;
    }

    public Transform rotate(Vector3fc rotation, Vector3fc pivot) {
        transformMatrix.rotateAroundLocal(new Quaternionf().rotateXYZ(rotation.x(), rotation.y(), rotation.z()), pivot.x(), pivot.y(), pivot.z());
        return this;
    }

    public Transform rotate(Quaternionf rotation) {
        transformMatrix.rotateLocal(rotation);
        return this;
    }

    public Transform rotate(Vector3fc rotation) {
        rotate(rotation, getTranslation());
        return this;
    }

    public Transform rotate(float x, float y, float z) {
        rotate(new Vector3f(x, y, z));
        return this;
    }

    public Transform scale(Vector3fc scale, Vector3fc pivot) {
        transformMatrix.scaleAroundLocal(scale.x() + 1, scale.y() + 1, scale.z() + 1, pivot.x(), pivot.y(), pivot.z());
        return this;
    }

    public Transform scale(Vector3fc scale) {
        scale(scale, getTranslation());
        return this;
    }

    public Transform scale(float x, float y, float z) {
        scale(new Vector3f(x, y, z), getTranslation());
        return this;
    }

    public Transform scale(float x, float y) {
        scale(new Vector2f(x, y), new Vector2f(getTranslation()));
        return this;
    }

    public Transform scale(Vector2fc scale, Vector2fc center) {
        scale(new Vector3f(scale, 0), new Vector3f(center, 0));
        return this;
    }

    public Transform scale(Vector2fc scale) {
        scale(scale.x(), scale.y());
        return this;
    }

    public Transform rotateX(float radians) {
        rotate(radians, 0, 0);
        return this;
    }

    public Transform rotateY(float radians) {
        rotate(0, radians, 0);
        return this;
    }

    public Transform rotateZ(float radians) {
        rotate(0, 0, radians);
        return this;
    }

    public Matrix4f getTransformMatrix() {
        return transformMatrix;
    }

    public Matrix4f getInvertedTransformMatrix() {
        return transformMatrix.invert(new Matrix4f());
    }

    public Matrix4f getTransformMatrix(Vector3fc sizeMul) {
        Matrix4f transformMatrix = new Matrix4f();
        transformMatrix.translationRotateScale(getTranslation(), getNormalizedRotation(), new Vector3f(getScale()).mul(sizeMul));
        return transformMatrix;
    }

    public Vector3fc getTranslation() {
        return transformMatrix.getTranslation(new Vector3f());
    }

    public Quaternionf getNormalizedRotation() {
        return transformMatrix.getNormalizedRotation(new Quaternionf());
    }

    public Vector3fc getScale() {
        return transformMatrix.getScale(new Vector3f());
    }

    @Override
    public String toString() {
        return "Transform{" +
                "translation=" + getTranslation() +
                ", normalizedRotation=" + getNormalizedRotation() +
                ", scale=" + getScale() + "}";
    }
}
