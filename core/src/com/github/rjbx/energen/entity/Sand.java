package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Sand extends Ground implements Pourous {

    // fields
    public final static String TAG = Sand.class.getName();

    private final Vector2 position;
    private final long startTime;

    // ctor
    public Sand(Vector2 position) {
        this.position = position;
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public Sand safeClone() {
        Sand clone = new Sand(position);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().sink.getKeyFrame(Helpers.secondsSince(startTime)), position, Constants.SINK_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SINK_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.SINK_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.SINK_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.SINK_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.SINK_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.SINK_CENTER.y; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_LOW;
    }
}
