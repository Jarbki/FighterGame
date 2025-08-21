package com.fighter.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fighter.animations.Animation;

import java.util.HashMap;

public class SpriteHandler {
    private HashMap<String, Animation> animations = new HashMap<>();
    private Animation activeAnimation;
    private String activeName;

    public void addAnimation(String name, Animation animation) {
        animations.put(name, animation);
        if (activeAnimation == null) {
            setAnimation(name);
        }
    }

    public void setAnimation(String name) {
        if (!name.equals(activeName)) {
            activeAnimation = animations.get(name);
            activeAnimation.reset();
            activeName = name;
        }
    }

    public void update(float delta) {
        if (activeAnimation != null) activeAnimation.update(delta);
    }

    public TextureRegion getCurrentFrame() {
        return activeAnimation != null ? activeAnimation.getCurrentFrame() : null;
    }
}
