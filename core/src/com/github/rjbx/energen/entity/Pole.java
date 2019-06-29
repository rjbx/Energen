package com.github.rjbx.energen.entity;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Pole extends Ground implements Unsteady, Climbable {

    // fields
    public final static String TAG = Pole.class.getName();

    private Vector2 position;
    private long startTime;

    // ctor
    public Pole(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public Pole safeClone() {
        Pole clone = new Pole(position);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().pole.getKeyFrame(Helpers.secondsSince(startTime), true), position, Constants.POLE_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.POLE_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.POLE_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.POLE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.POLE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.POLE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.POLE_CENTER.y; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
