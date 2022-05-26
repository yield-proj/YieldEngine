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

public class Line extends Component {
    private Vector2 point2 = new Vector2(10, 10);
    private Color color = Colors.CYAN;

    @Override
    public void render(SampleGraphics graphics) {
        graphics.drawLine(transform.position, point2.mul(transform.scale), color);
    }

    public Vector2 getPoint2() {
        return point2;
    }

    public void setPoint2(Vector2 point2) {
        this.point2 = point2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
