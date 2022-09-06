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

/**
 * It renders the entity with a rotation and a color
 */
public class SimpleRenderable extends Component {
    private boolean forceAngle;
    private Color color = Colors.CYAN;

    private float angle;

    private Vector2 addAnchorPoint = new Vector2();

    private boolean ignoreViewPosition;

    @Override
    public void render(SampleGraphics graphics) {
        float angle = getEntity().getTransform().rotation + this.angle;
        if (forceAngle)
            angle = this.angle;
        Vector2 pos = getEntity().getTransform().position.sum(addAnchorPoint);
        if (!ignoreViewPosition) {
            pos = pos.subt(scene.getView().getTransform().position);
        }
        graphics.setRotation(pos, angle);
    }

    /**
     * This function returns the color of the object.
     *
     * @return The color of the car.
     */
    public Color getColor() {
        return color;
    }

    /**
     * This function sets the color of the object to the color passed in as a parameter.
     *
     * @param color The color of the renderable.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns whether the angle of the vector will override the entity angle.
     *
     * @return The value of the forceAngle variable.
     */
    public boolean isForceAngle() {
        return forceAngle;
    }

    /**
     * Sets if the angle of the vector will override the entity angle.
     *
     * @param forceAngle If true, the angle of the force will be the same as the angle of the object. If false, the angle
     * of the force will be the angle of the object plus the angle variable.
     */
    public void setForceAngle(boolean forceAngle) {
        this.forceAngle = forceAngle;
    }

    /**
     * This function sets the angle of the object to the value of the parameter angle.
     *
     * @param angle The angle of the rotation in degrees.
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

    /**
     * This function returns the angle of the object.
     *
     * @return The angle of the object.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Returns the position of the add anchor point.
     *
     * @return The addAnchorPoint variable is being returned.
     */
    public Vector2 getAddAnchorPoint() {
        return addAnchorPoint;
    }

    /**
     * This function sets the addAnchorPoint variable to the value of the addAnchorPoint parameter.
     *
     * @param addAnchorPoint The addAnchorPoint value to set.
     */
    public void setAddAnchorPoint(Vector2 addAnchorPoint) {
        this.addAnchorPoint = addAnchorPoint;
    }

    /**
     * Returns whether the view position is ignored.
     *
     * @return A boolean value.
     */
    public boolean isIgnoreViewPosition() {
        return ignoreViewPosition;
    }

    /**
     * Sets whether the view position should be ignored when rendering.
     *
     * @param ignoreViewPosition If true, the render position will not be affected by the view position.
     */
    public void setIgnoreViewPosition(boolean ignoreViewPosition) {
        this.ignoreViewPosition = ignoreViewPosition;
    }
}
