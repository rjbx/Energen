package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Pod extends Ground implements Reboundable, Replenishing {

    // fields
    public final static String TAG = Pod.class.getName();

    private Vector2 position;
    private long startTime;
    private boolean isActive;

    // ctor
    public Pod(Vector2 position) {
        this.position = position;
        isActive = false;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (isActive) {
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().activePod.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.POD_CENTER);
        } else {
            Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().pod, position, Constants.POD_CENTER);
        }
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Enums.PowerupType getType() { return Enums.PowerupType.HEALTH; }
    @Override public final float getHeight() { return Constants.POD_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.POD_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.POD_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.POD_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.POD_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.POD_CENTER.y; }
    @Override public final boolean isDense() { return false; }
    @Override public final long getStartTime() { return startTime; }
    @Override public final void setState(boolean state) { this.isActive = state; }
    @Override public final boolean getState() { return isActive; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public final float jumpMultiplier() { return Constants.POD_JUMP_MULTIPLIER; }
}
