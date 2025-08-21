package com.fighter.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    private TextureRegion[] frames;
    private float frameDuration;
    private float elapsedTime = 0f;

    public Animation(TextureRegion[] frames, float frameDuration) {
        this.frames = frames;
        this.frameDuration = frameDuration;
    }

    public void update(float delta) {
        elapsedTime += delta;
    }

    public TextureRegion getCurrentFrame() {
        int frameIndex = (int)(elapsedTime / frameDuration) % frames.length;
        return frames[frameIndex];
    }

    public void reset() {
        elapsedTime = 0f;
    }
}
