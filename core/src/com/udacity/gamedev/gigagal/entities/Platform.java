package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;

// immutable
public final class Platform implements Physical {

    // fields
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;

    // This is used by the level loading code to link enemies and platforms.
    private static String identifier;

    // ctor
    public Platform(float left, float top, float width, float height) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
    }

    // copy ctor
    public Platform(Platform p) {
        this.top = p.getTop();
        this.bottom = p.getBottom();
        this.left = p.getLeft();
        this.right = p.getRight();
    }

    public void render(SpriteBatch batch) {
        final float width = right - left;
        final float height = top - bottom;
        Assets.getInstance().getPlatformAssets().platformNinePatch.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
    }

    // Getters
    public float getTop() { return top; }
    public float getBottom() {return bottom; }
    public float getLeft() { return left; }
    public float getRight() { return right; }
    public Vector2 getPosition() { return new Vector2(left + (getWidth() / 2), bottom + (getHeight() / 2)); }
    public float getWidth() { return right - left;}
    public float getHeight() {return top - bottom; }
    public static final String getIdentifier() { return identifier; }
}
