package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Rope extends Ground implements Climbable {

    // fields
    public final static String TAG = Rope.class.getName();

    private Vector2 position;

    // ctor
    public Rope(Vector2 position) {
        this.position = position;
    }

    @Override
    public Rope safeClone() {
        Rope clone = new Rope(position);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().rope, position, Constants.ROPE_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.ROPE_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.ROPE_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.ROPE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROPE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROPE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROPE_CENTER.y; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
