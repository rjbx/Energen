package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Vines extends Ground implements Climbable, Unsteady {

    // fields
    public final static String TAG = Vines.class.getName();

    private Vector2 position;
    private Vector2 adjustedCenter;
    private Vector2 scale;

    // ctor
    public Vines(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.adjustedCenter = adjustedCenter;
        this.scale = scale;
    }

    @Override
    public Vines safeClone() {
        Vines clone = new Vines(position, scale, adjustedCenter);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, AssetManager.getInstance().getGroundAssets().vines, position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.VINES_CENTER.y * 2 * scale.y; }
    @Override public final float getWidth() { return Constants.VINES_CENTER.x * 2 * scale.x; }
    @Override public final float getLeft() { return position.x - Constants.VINES_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.VINES_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.VINES_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.VINES_CENTER.y * scale.y; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
