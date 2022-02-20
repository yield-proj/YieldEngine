package com.xebisco.yield.components;

import com.xebisco.yield.Animation;
import com.xebisco.yield.Component;
import com.xebisco.yield.Yld;

import java.util.ArrayList;

public class Animator extends Component {

    private ArrayList<Animation> animations = new ArrayList<>();
    private Animation actAnim;
    private int actAnimFrame;

    @Override
    public void update(float delta) {
        if (actAnim != null) {
            actAnimFrame++;
            if(actAnimFrame >= actAnim.getFrameDelay()) {
                actAnimFrame = 0;
                actAnim.setActFrame(actAnim.getActFrame() + 1);
                if(actAnim.getActFrame() >= actAnim.getFrames().length) {
                    actAnim.setActFrame(0);
                }
            }
            getMaterial().setTexture(actAnim.getFrames()[actAnim.getActFrame()]);
        }
    }

    public void setAnimation(String animName) {
        boolean finded = false;
        for (Animation animation : animations) {
            if (animation.getName().hashCode() == animName.hashCode() && animation.getName().equals(animName)) {
                finded = true;
                setActAnim(animation);
                actAnimFrame = 0;
                break;
            }
        }
        if (!finded)
            throw new IllegalArgumentException("'" + animName + "' is not a animation");
    }

    public void addAnimation(Animation animation) {
        animations.add(animation);
    }

    public Animation getActAnim() {
        return actAnim;
    }

    public void setActAnim(Animation actAnim) {
        this.actAnim = actAnim;
    }

    public ArrayList<Animation> getAnimations() {
        return animations;
    }

    public void setAnimations(ArrayList<Animation> animations) {
        this.animations = animations;
    }

    public int getActAnimFrame() {
        return actAnimFrame;
    }

    public void setActAnimFrame(int actAnimFrame) {
        this.actAnimFrame = actAnimFrame;
    }
}
