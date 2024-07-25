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

import org.joml.*;

import java.io.Serializable;

public class Transform implements Serializable {
    private static final long serialVersionUID = 2642369926201568158L;
    private final Vector3fc position = new Vector3f(), rotation = new Vector3f(), scale = new Vector3f(1, 1, 1), frameTranslation = new Vector3f(), frameRotation = new Vector3f(), frameScale = new Vector3f();

    public Transform(Transform transform) {
        ((Vector3f) position).set(transform.position);
        ((Vector3f) rotation).set(transform.rotation);
        ((Vector3f) scale).set(transform.scale);
        ((Vector3f) frameTranslation).set(transform.frameTranslation);
        ((Vector3f) frameRotation).set(transform.frameRotation);
        ((Vector3f) frameScale).set(transform.frameScale);
    }

    public Transform() {
    }

    public Transform translate(Vector3fc translation) {
        ((Vector3f) position).add(translation);
        ((Vector3f) frameTranslation).add(translation);
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

    public Transform rotate(Vector3fc rotation) {
        ((Vector3f) this.rotation).add(rotation);
        ((Vector3f) frameRotation).add(rotation);
        return this;
    }

    public Transform rotate(float x, float y, float z) {
        rotate(new Vector3f(x, y, z));
        return this;
    }

    public Transform rotate(float z) {
        rotate(new Vector3f(0, 0, z));
        return this;
    }

    public Transform scale(Vector3fc scale) {
        ((Vector3f) this.scale).add(scale);
        ((Vector3f) frameScale).add(scale);
        return this;
    }

    public Transform scale(float x, float y, float z) {
        scale(new Vector3f(x, y, z));
        return this;
    }

    public Transform scale(float x, float y) {
        scale(new Vector3f(x, y, 0));
        return this;
    }

    public Transform scale(Vector2fc scale) {
        scale(scale.x(), scale.y());
        return this;
    }

    public Transform rotate(Vector3fc rotation, Vector3fc center) {
        Vector3f modifiedPosition = new Vector3f(), modifiedRotation = new Vector3f();

        rotateX(rotation.x(), new Vector2f(center.y(), center.z()));
        rotateY(rotation.y(), new Vector2f(center.x(), center.z()));
        rotateZ(rotation.z(), new Vector2f(center.x(), center.y()));

        translate(modifiedPosition);
        rotate(modifiedRotation);
        return this;
    }

    private static void positionAfterCustomRotation(float radians, Vector2fc center, Vector2f modifiedPosition) {
        Vector2fc rotatedXY = Global.rotatePointAroundCenter(radians, center, new Vector2f(modifiedPosition.x(), modifiedPosition.y()));

        modifiedPosition.x = rotatedXY.x();
        modifiedPosition.y = rotatedXY.y();
    }

    public Transform rotateX(float radians, Vector2fc center) {
        Vector2f modifiedPosition = new Vector2f();
        positionAfterCustomRotation(radians, center, modifiedPosition);
        //noinspection SuspiciousNameCombination
        translate(0, modifiedPosition.x, modifiedPosition.y);
        rotate(radians, 0, 0);
        return this;
    }

    public Transform rotateY(float radians, Vector2fc center) {
        Vector2f modifiedPosition = new Vector2f();
        positionAfterCustomRotation(radians, center, modifiedPosition);
        translate(modifiedPosition.x, 0, modifiedPosition.y);
        rotate(0, radians, 0);
        return this;
    }

    public Transform rotateZ(float radians, Vector2fc center) {
        Vector2f modifiedPosition = new Vector2f();
        positionAfterCustomRotation(radians, center, modifiedPosition);
        translate(modifiedPosition.x, modifiedPosition.y, 0);
        rotate(0, 0, radians);
        return this;
    }

    public Matrix4f getTransformationMatrix() {
        return getTransformationMatrix(new Vector3f(1, 1, 1));
    }

    public Matrix4f getTransformationMatrix(Vector3fc scaleMul) {
        Matrix4f transformationMatrix = new Matrix4f().identity();
        transformationMatrix.translate(position);
        transformationMatrix.rotateXYZ(rotation);
        transformationMatrix.scale(new Vector3f(scale.x() * scaleMul.x(), scale.y() * scaleMul.y(), scale.z() * scaleMul.z()));
        return transformationMatrix;
    }

    public Matrix4f getInvertedAndPreRotatedTransformationMatrix() {
        Matrix4f transformationMatrix = new Matrix4f().identity();
        transformationMatrix.rotateXYZ(new Vector3f(-rotation.x(), -rotation.y(), -rotation.z()));
        transformationMatrix.translate(new Vector3f(-position.x(), -position.y(), -position.z()));
        transformationMatrix.scale(new Vector3f(1 / scale.x(), 1 / scale.y(), 1 / scale.z()));
        return transformationMatrix;
    }

    protected Transform resetFrame() {
        ((Vector3f) frameTranslation).set(0, 0, 0);
        ((Vector3f) frameRotation).set(0, 0, 0);
        ((Vector3f) frameScale).set(0, 0, 0);
        return this;
    }

    public Vector3fc getPosition() {
        return position;
    }

    public Vector3fc getRotation() {
        return rotation;
    }

    public Vector3fc getScale() {
        return scale;
    }

    public Vector3fc getFrameTranslation() {
        return frameTranslation;
    }

    public Vector3fc getFrameRotation() {
        return frameRotation;
    }

    public Vector3fc getFrameScale() {
        return frameScale;
    }
}
