package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

// immutable
public final class Portal extends Transport {

    // fields
    public final static String TAG = Portal.class.getName();
    private final Vector2 position;
    private final Vector2 destination;
    private final long startTime;
    private final boolean goal;

    //ctor
    public Portal(Vector2 position, boolean goal) {
        this.position = position;
        this.destination = position;
        this.goal = goal;
        startTime = TimeUtils.nanoTime();
    }

    public Portal safeClone() {
        Portal clone = new Portal(position, goal);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getPortalAssets().portal.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.PORTAL_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final Vector2 getDestination() { return destination; }
    @Override public final float getHeight() { return Constants.PORTAL_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.PORTAL_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.PORTAL_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.PORTAL_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.PORTAL_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.PORTAL_CENTER.y; }
    public boolean isGoal() { return goal; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}

