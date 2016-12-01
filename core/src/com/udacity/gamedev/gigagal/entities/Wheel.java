package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Wheel extends Indestructible {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Wheel(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getWheelAssets().wheel.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.WHEEL_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getWidth() { return Constants.WHEEL_COLLISION_WIDTH; }
    public final float getHeight() { return Constants.WHEEL_COLLISION_HEIGHT; }
    public final float getLeft() { return position.x - Constants.WHEEL_CENTER.x; }
    public final float getRight() { return position.x + Constants.WHEEL_CENTER.x; }
    public final float getTop() { return position.y + Constants.WHEEL_CENTER.y; }
    public final float getBottom() { return position.y - Constants.WHEEL_CENTER.y; }
    public final int getDamage() { return Constants.WHEEL_DAMAGE; }
    public final Vector2 getKnockback() { return Constants.WHEEL_KNOCKBACK; }
    public final Class getSubclass() { return this.getClass(); }
}
