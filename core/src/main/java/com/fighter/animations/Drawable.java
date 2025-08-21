package com.fighter.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Drawable {
    void update(float dt);
    void render(SpriteBatch batch);
    void dispose();
}
