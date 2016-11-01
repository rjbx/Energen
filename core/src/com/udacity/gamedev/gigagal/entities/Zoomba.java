package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums.DirectionalState;
import com.udacity.gamedev.gigagal.util.Utils;


public class Zoomba {

    private final long startTime;
    private final float bobOffset;
    private final Platform platform;
    public Vector2 position;
    public int health;
    private DirectionalState directionalState;

    public Zoomba(Platform platform) {
        this.platform = platform;
        directionalState = DirectionalState.RIGHT;
        position = new Vector2(platform.left, platform.top + Constants.ZOOMBA_CENTER.y);
        startTime = TimeUtils.nanoTime();
        health = Constants.ZOOMBA_HEALTH;
        bobOffset = MathUtils.random();
    }

    public void update(float delta) {
        switch (directionalState) {
            case LEFT:
                position.x -= Constants.ZOOMBA_MOVEMENT_SPEED * delta;
                break;
            case RIGHT:
                position.x += Constants.ZOOMBA_MOVEMENT_SPEED * delta;
        }

        if (position.x < platform.left) {
            position.x = platform.left;
            directionalState = DirectionalState.RIGHT;
        } else if (position.x > platform.right) {
            position.x = platform.right;
            directionalState = DirectionalState.LEFT;
        }

        final float elapsedTime = Utils.secondsSince(startTime);
        final float bobMultiplier = 1 + MathUtils.sin(MathUtils.PI2 * (bobOffset + elapsedTime / Constants.ZOOMBA_BOB_PERIOD));
        position.y = platform.top + Constants.ZOOMBA_CENTER.y + Constants.ZOOMBA_BOB_AMPLITUDE * bobMultiplier;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.instance.zoombaAssets.zoomba;
        Utils.drawTextureRegion(batch, region, position, Constants.ZOOMBA_CENTER);
    }
}
