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

package com.xebisco.yield.systems;

import com.xebisco.yield.Component;
import com.xebisco.yield.ProcessSystem;
import com.xebisco.yield.components.RectCollider;
import java.util.ArrayList;

public class PhysicsSystem extends ProcessSystem
{
    @Override
    public String[] componentFilters()
    {
        return new String[]{RectCollider.class.getName()};
    }

    private final ArrayList<RectCollider> colliders = new ArrayList<>();

    @Override
    public void process(Component component, float delta)
    {
        if (component instanceof RectCollider)
        {
            RectCollider rectCollider = (RectCollider) component;
            if (!colliders.contains(rectCollider))
                colliders.add(rectCollider);
            RectCollider closedCollider = isClosed(rectCollider);
            if (closedCollider != null)
            {
                boolean sameLayer = false;
                for (int i = 0; i < rectCollider.getLayers().length; i++)
                {
                    if (closedCollider.getLayers().length < i)
                        break;
                    if (rectCollider.getLayers()[i] == closedCollider.getLayers()[i])
                    {
                        sameLayer = (true);
                        break;
                    }
                }
                if (sameLayer)
                {
                    rectCollider.getEntity().getSelfTransform().translate(-rectCollider.getSelfTransform().getTransformed().x, -rectCollider.getSelfTransform().getTransformed().y);
                    rectCollider.getEntity().transmit("onCollisionEnter", closedCollider);
                    closedCollider.getEntity().transmit("onCollisionEnter", rectCollider);
                }
            }
        }
    }

    public RectCollider isClosed(RectCollider collider)
    {
        for (RectCollider c : colliders)
        {
            if (c != collider)
            {
                if (collider.getUpLeft().x <= c.getUpLeft().x + c.getSize().x
                        && collider.getUpLeft().x + collider.getSize().x >= c.getUpLeft().x
                        && collider.getUpLeft().y <= c.getUpLeft().y + c.getSize().y
                        && collider.getUpLeft().y + collider.getSize().y >= c.getUpLeft().y)
                {
                    return c;
                }
            }
        }
        return null;
    }
}
