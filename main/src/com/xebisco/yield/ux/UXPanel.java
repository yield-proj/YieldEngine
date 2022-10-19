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

package com.xebisco.yield.ux;

import com.xebisco.yield.*;
import com.xebisco.yield.exceptions.UXException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class UXPanel extends UXRect {
    private final Set<UXComponent> components = new HashSet<>();

    public UXPanel(Vector2 position, Vector2 size, UXMain uxMain) {
        super(position, size, uxMain);
        setBackground(getUxMain().getPalette().background1);
    }

    public Set<UXComponent> getComponents() {
        return components;
    }

    public <T extends UXComponent> T add(Class<T> componentType, Object... args) throws UXException {
        Class<?>[] constructorTypes = new Class[args.length + 1];
        Object[] argsC = new Object[args.length + 1];
        System.arraycopy(args, 0, argsC, 0, args.length);
        argsC[argsC.length - 1] = getUxMain();
        args = argsC;
        for (int i = 0; i < argsC.length; i++)
            constructorTypes[i] = argsC[i].getClass();
        T t;
        try {
            t = componentType.getConstructor(constructorTypes).newInstance(args);
            t.setPanel(this);
            getComponents().add(t);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
            Yld.throwException(new UXException(e.getClass().getSimpleName() + ": " + e.getMessage()));
        }
        return null;
    }

    @Override
    public void render(SampleGraphics graphics, float delta) {
        graphics.setFilter(Filter.LINEAR);
        super.render(graphics, delta);
        components.forEach(c -> c.render(graphics, delta));
    }
}
