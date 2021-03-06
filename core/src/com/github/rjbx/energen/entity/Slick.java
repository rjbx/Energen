package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Slick extends Ground implements Rappelable, Skateable {
    
    // fields
    public final static String TAG = Slick.class.getName();

    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;

    // ctor
    public Slick(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public Slick safeClone() {
        Slick clone = new Slick(position, scale, adjustedCenter);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().slick.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }
    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.SLICK_CENTER.y * 2 * scale.y; }
    @Override public final float getWidth() { return Constants.SLICK_CENTER.x * 2 * scale.x; }
    @Override public final float getLeft() { return position.x - Constants.SLICK_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.SLICK_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.SLICK_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.SLICK_CENTER.y * scale.y; }
    @Override public final boolean isDense() { return getHeight() > Constants.MAX_LEDGE_HEIGHT; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_OVERRIDE;
    }
}
