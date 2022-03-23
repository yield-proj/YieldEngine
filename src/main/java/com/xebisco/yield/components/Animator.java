/*
 * Copyright [2022] [Xebisco]
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

package com.xebisco.yield.components;

import com.xebisco.yield.Animation;
import com.xebisco.yield.Component;
import java.util.ArrayList;

public class Animator extends Component
{

    private ArrayList<Animation> animations = new ArrayList<>();
    private Animation actAnim;
    private int actAnimFrame;

    @Override
    public void update(float delta)
    {
        if (actAnim != null)
        {
            if (actAnim.getMicrosecondDelay() == 0)
            {
                actAnimFrame++;
                if (actAnimFrame >= actAnim.getFrameDelay())
                {
                    actAnimFrame = 0;
                    actAnim.setActFrame(actAnim.getActFrame() + 1);
                    if (actAnim.getActFrame() >= actAnim.getFrames().length)
                    {
                        actAnim.setActFrame(0);
                    }
                }
            }
            else
            {
                actAnimFrame += (delta * 1000f);
                if (actAnimFrame >= actAnim.getMicrosecondDelay())
                {
                    actAnimFrame = 0;
                    actAnim.setActFrame(actAnim.getActFrame() + 1);
                    if (actAnim.getActFrame() >= actAnim.getFrames().length)
                    {
                        actAnim.setActFrame(0);
                    }
                }
            }
            getMaterial().setTexture(actAnim.getFrames()[actAnim.getActFrame()]);
        }
    }

    public void setAnimation(String animName)
    {
        boolean finded = false;
        for (Animation animation : animations)
        {
            if (animation.getName().hashCode() == animName.hashCode() && animation.getName().equals(animName))
            {
                finded = true;
                setActAnim(animation);
                actAnimFrame = 0;
                break;
            }
        }
        if (!finded)
            throw new IllegalArgumentException("'" + animName + "' is not a animation");
    }

    public void setAnimation(Animation anim)
    {
        setActAnim(anim);
        actAnimFrame = 0;
    }

    public void addAnimation(Animation animation)
    {
        animations.add(animation);
    }

    public Animation getActAnim()
    {
        return actAnim;
    }

    public void setActAnim(Animation actAnim)
    {
        this.actAnim = actAnim;
    }

    public ArrayList<Animation> getAnimations()
    {
        return animations;
    }

    public void setAnimations(ArrayList<Animation> animations)
    {
        this.animations = animations;
    }

    public int getActAnimFrame()
    {
        return actAnimFrame;
    }

    public void setActAnimFrame(int actAnimFrame)
    {
        this.actAnimFrame = actAnimFrame;
    }
}
