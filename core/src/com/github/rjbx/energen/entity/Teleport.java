package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Teleport extends Transport {

    //fields
    public final static String TAG = Teleport.class.getName();

    private final boolean goal;
    private Vector2 position;
    private Vector2 destination;
    private long startTime;

    public Teleport(Vector2 position, Vector2 destination, boolean goal) {
        this.position = position;
        this.destination = destination;
        this.startTime = TimeUtils.nanoTime();
        this.goal = goal;
    }

    @Override
    public Teleport safeClone() {
        Teleport clone = new Teleport(position, destination, goal);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getPortalAssets().teleport.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.TELEPORT_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.TELEPORT_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.TELEPORT_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.TELEPORT_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.TELEPORT_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.TELEPORT_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.TELEPORT_CENTER.y; }
    @Override public final Vector2 getDestination() { return destination; }
    public boolean isGoal() { return goal; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
