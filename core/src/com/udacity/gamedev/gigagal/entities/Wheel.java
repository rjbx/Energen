package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Wheel implements IndestructibleHazard {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Wheel(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getWheelAssets().wheel.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.WHEEL_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getWidth() { return Constants.WHEEL_COLLISION_WIDTH; }
    @Override public final float getHeight() { return Constants.WHEEL_COLLISION_HEIGHT; }
    @Override public final float getLeft() { return position.x - Constants.WHEEL_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.WHEEL_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.WHEEL_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.WHEEL_CENTER.y; }
    @Override public final int getDamage() { return Constants.WHEEL_DAMAGE; }
    @Override public final Vector2 getKnockback() { return Constants.WHEEL_KNOCKBACK; }
}
