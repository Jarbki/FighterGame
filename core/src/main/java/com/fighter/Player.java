package com.fighter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.fighter.animations.Drawable;
import com.fighter.input.InputState;
import com.fighter.input.KeyboardController;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.HashMap;

public class Player implements Drawable {

    private static final int FRAME_W = 128;
    private static final int FRAME_H = 128;

    private final Texture sheet;
    private final HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    private Animation<TextureRegion> currentAnim;
    private final KeyboardController input = new KeyboardController();

    private boolean animLock = false;
    private float stateTime = 0f;
    private boolean onGround = true;
    private float x, y;
    private float vx = 220f;   // pixels/sec
    private float vy = 0;
    private float gravity = -1000;
    private float jumpSpeed = 400;
    boolean jumpStarted = false;
    private boolean facingRight = true;

    public Player(String spriteSheetPath, float startX, float startY) {
        this.sheet = new Texture(spriteSheetPath);
        TextureRegion[][] grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

        // Build animations (fixing arraycopy mistakes)
        animations.put("attackOne", makeAnim(new TextureRegion[]{
            grid[0][0], grid[0][1], grid[0][2], grid[0][3]
        }, 0.15f, false));

        animations.put("walk", makeAnim(new TextureRegion[]{
            grid[0][4], grid[0][5], grid[0][6],
            grid[1][0], grid[1][1], grid[1][2], grid[1][3], grid[1][4]
        }, 0.08f, true));

        animations.put("attackTwo", makeAnim(new TextureRegion[]{
            grid[1][5], grid[1][6], grid[2][0]
        }, 0.15f, false));

        animations.put("attackThree", makeAnim(new TextureRegion[]{
            grid[2][1], grid[2][2], grid[2][3], grid[2][4]
        }, 0.15f, false));

        animations.put("dead", makeAnim(new TextureRegion[]{
            grid[2][5], grid[2][6], grid[3][0]
        }, 0.15f, false));

        animations.put("hurt", makeAnim(new TextureRegion[]{
            grid[3][1], grid[3][2], grid[3][3]
        }, 0.15f, false));

        animations.put("idle", makeAnim(new TextureRegion[]{
            grid[3][4], grid[3][5], grid[3][6],
            grid[4][0], grid[4][1], grid[4][2]
        }, 0.15f, true));

        animations.put("jump", makeAnim(new TextureRegion[]{
            grid[4][3], grid[4][4], grid[4][5], grid[4][6],
            grid[5][0], grid[5][1], grid[5][2], grid[5][3], grid[5][4], grid[5][5]
        }, 0.12f, false));

        animations.put("run", makeAnim(new TextureRegion[]{
            grid[5][6],
            grid[6][0], grid[6][1], grid[6][2], grid[6][3], grid[6][4], grid[6][5], grid[6][6]
        }, 0.08f, true));

        animations.put("shield", makeAnim(new TextureRegion[]{
            grid[7][0], grid[7][1]
        }, 0.15f, true));

        currentAnim = animations.get("idle");

        this.x = startX;
        this.y = startY;
    }

    private Animation<TextureRegion> makeAnim(TextureRegion[] frames, float frameDuration, boolean loop) {
        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);
        return anim;
    }

    @Override
    public void update(float dt) {
        InputState in = input.poll();

        // Unlock if current non-looping animation finished
        if (!isLooping(currentAnim) && currentAnim.isAnimationFinished(stateTime)) {
            animLock = false;
            setAnimation("idle", false);
        }

        // Movement
        float dx = in.axisX() * vx * dt;

        vy += gravity * dt;
        y += vy * dt;
        x = MathUtils.clamp(x + dx, 0, FighterGame.V_WIDTH - FRAME_W);

        if (y <= 0){
            y = 0;
            vy = 0;
            if (!onGround && jumpStarted){
                System.out.println("On ground");
                onGround = true;
                animLock = false;
                setAnimation("idle", false);
            }
        }

        // Facing direction
        if (dx < 0) facingRight = false;
        if (dx > 0) facingRight = true;

        // Animation choice
        if (!animLock) {
            if (in.attackOne()) setAnimation("attackOne", true);
            else if (in.attackTwo()) setAnimation("attackTwo", true);
            else if (in.attackThree()) setAnimation("attackThree", true);
            else if (in.jump()) {
                setAnimation("jump", true);
                onGround = false;
                jumpStarted = false;
            } else if (Math.abs(dx) > 0.001f) setAnimation(in.run() ? "run" : "walk", false);
            else if (in.shield()) setAnimation("shield", false);
            else setAnimation("idle", false);
        }

        if (currentAnim == animations.get("jump")){
            int frameIndex = currentAnim.getKeyFrameIndex(stateTime);
            if (frameIndex == 2 && !jumpStarted){
                vy = jumpSpeed;
                jumpStarted = true;
            }
        }
        stateTime += dt;
    }

    private void setAnimation(String key, boolean lock) {
        Animation<TextureRegion> anim = animations.get(key);
        if (anim != null && anim != currentAnim) {
            currentAnim = anim;
            stateTime = 0f;
            animLock = lock;
        }
    }

    private boolean isLooping(Animation<TextureRegion> anim) {
        return anim.getPlayMode() == Animation.PlayMode.LOOP || anim.getPlayMode() == Animation.PlayMode.LOOP_PINGPONG;
    }

    @Override
    public void render(SpriteBatch batch) {
        TextureRegion frame = currentAnim.getKeyFrame(stateTime);
        float drawX = facingRight ? x : x + FRAME_W;
        float drawW = facingRight ? FRAME_W : -FRAME_W;
        batch.draw(frame, drawX, y, drawW, FRAME_H);
    }

    @Override
    public void dispose() {
        sheet.dispose();
    }
}
