/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xebisco.yield;

public class TwoAnchorRepresentation {
    private double x, y;
    public TwoAnchorRepresentation(double x, double y) {
        set(x, y);
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void sumLocal(TwoAnchorRepresentation a) {
        x += a.x;
        y += a.y;
    }

    public void subtractLocal(TwoAnchorRepresentation a) {
        x -= a.x;
        y -= a.y;
    }

    public void multiplyLocal(TwoAnchorRepresentation a) {
        x *= a.x;
        y *= a.y;
    }

    public void divideLocal(TwoAnchorRepresentation a) {
        x /= a.x;
        y /= a.y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
