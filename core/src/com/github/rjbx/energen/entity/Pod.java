package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.app.LevelAssets;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;

public class Pod extends Ground implements Reboundable, Replenishing, Compressible, Groundable {

    // fields
    public final static String TAG = Pod.class.getName();

    private Vector2 position;
    private Ground topGround;
    private long startTime;
    private boolean isActive;
    private boolean beneatheGround;

    // ctor
    public Pod(Vector2 position) {
        this.position = position;
        isActive = false;
        beneatheGround = false;
        startTime = TimeUtils.nanoTime();
    }

    public Pod safeClone() {
        Pod clone = new Pod(position);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void update(float delta) {
        topGround = null;
        for (Ground ground : LevelAssets.getClonedGrounds()) {
            if (Helpers.overlapsPhysicalObject(this, ground)) {
                if (Helpers.betweenTwoValues(getTop(), ground.getBottom() - 2, ground.getBottom() + 2)) {
                    isActive = true;
                    beneatheGround = true;
                    topGround = ground;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        if (isActive) {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().activePod.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.POD_CENTER);
        } else {
            Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().pod, position, Constants.POD_CENTER);
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
    @Override public final boolean isDense() { return true; }
    @Override public final float jumpMultiplier() { return Constants.POD_JUMP_MULTIPLIER; }
    @Override public final void setState(boolean state) { this.isActive = state; }
    @Override public final boolean getState() { return isActive; }
    @Override public final long getStartTime() { return startTime; }
    @Override public final void resetStartTime() { this.startTime = 0; }
    @Override public final boolean isBeneatheGround() { return beneatheGround; }
    @Override public final Ground getTopGround() { return topGround; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
