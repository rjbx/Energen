package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;

// mutable
public class Box implements SolidGround {

    // fields
    private final float top;
    private final float bottom;
    private final float left;
    private final float right;

    // This is used by the level loading code to link enemies and boxes.
    private static String identifier;

    // default ctor
    public Box() {
        top = 0;
        bottom = 0;
        left = 0;
        right = 0;
    }

    // ctor
    public Box(float left, float top, float width, float height) {
        this.top = top;
        this.bottom = top - height;
        this.left = left;
        this.right = left + width;
    }

    @Override
    public void render(SpriteBatch batch) {
        final float width = right - left;
        final float height = top - bottom;
        Assets.getInstance().getPlatformAssets().platformNinePatch.draw(batch, left - 1, bottom - 1, width + 2, height + 2);
    }

    // Getters
    @Override public float getTop() { return top; }
    @Override public float getBottom() {return bottom; }
    @Override public float getLeft() { return left; }
    @Override public float getRight() { return right; }
    @Override public Vector2 getPosition() { return new Vector2(left + (getWidth() / 2), bottom + (getHeight() / 2)); }
    @Override public float getWidth() { return right - left;}
    @Override public float getHeight() {return top - bottom; }
    public static final String getIdentifier() { return identifier; }
}
