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

public abstract class Shape extends Component
{

    private Renderer renderer;
    private Obj obj;
    private Color color = Colors.CYAN;

    @Override
    public void start()
    {
        super.start();
        renderer = getComponent(Renderer.class);
        Color previous = renderer.getGraphics().getColor();
        renderer.getGraphics().setColor(getColor());
        parameters(renderer.getGraphics());
        obj = renderer.getGraphics().sample();
        renderer.getGraphics().setColor(previous);
        previous(renderer.getGraphics());
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        obj.color = color;
        obj.rotationV = (int) getTransform().rotation;
        obj.rotationX = (int) getTransform().middle.x;
        obj.rotationY = (int) getTransform().middle.y;
        obj.index = getEntity().getEntityIndex();
        process(obj);
        renderer.getGraphics().shapeRends.add(obj);
    }

    @Override
    public void onDestroy()
    {
        scene.getGraphics().shapeRends.remove(obj);
        renderer.getGraphics().shapeRends.remove(obj);
    }

    public void setCentered(boolean centered)
    {
        obj.center = centered;
    }

    public abstract void process(Obj obj);

    public abstract void parameters(YldGraphics graphics);

    public abstract void previous(YldGraphics graphics);

    public Obj getObj()
    {
        return obj;
    }

    public void setObj(Obj obj)
    {
        this.obj = obj;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }
}