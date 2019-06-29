package com.github.rjbx.energen.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Helpers;

public class Ladder extends Ground implements Climbable {

    // fields
    public final static String TAG = Ladder.class.getName();

    private final float top;
    private final float bottom;
    private final float left;
    private final float right;
    private final float width;
    private final float height;

    // default ctor
    public Ladder() {
        top = 0;
        bottom = 0;
        left = 0;
        right = 0;
        width = 0;
        height = 0;
    }

    // ctor
    public Ladder(float left, float top, float width, float height) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
        this.width = width;
        this.height = height;
    }

    public Ladder safeClone() {
        Ladder clone = new Ladder(left, top, width, height);
        clone.setClonedHashCode(hashCode());
        return clone;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawNinePatch(batch, viewport, AssetManager.getInstance().getGroundAssets().ladderNinePatch, left - 1, bottom - 1, width, height);
    }

    // Getters
    @Override public float getTop() { return top; }
    @Override public float getBottom() {return bottom; }
    @Override public float getLeft() { return left; }
    @Override public float getRight() { return right; }
    @Override public Vector2 getPosition() { return new Vector2(left + (getWidth() / 2), bottom + (getHeight() / 2)); }
    @Override public float getWidth() { return right - left;}
    @Override public float getHeight() { return top - bottom; }
    @Override public final boolean isDense() { return false; }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_MEDIUM;
    }
}
