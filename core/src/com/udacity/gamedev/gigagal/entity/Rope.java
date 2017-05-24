package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Rope implements Climbable, Ground {

    // fields
    private Vector2 position;

    // ctor
    public Rope(Vector2 position) {
        this.position = position;
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().rope, position, Constants.ROPE_CENTER);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.ROPE_CENTER.y * 2; }
    @Override public final float getWidth() { return Constants.ROPE_CENTER.x * 2; }
    @Override public final float getLeft() { return position.x - Constants.ROPE_CENTER.x; }
    @Override public final float getRight() { return position.x + Constants.ROPE_CENTER.x; }
    @Override public final float getTop() { return position.y + Constants.ROPE_CENTER.y; }
    @Override public final float getBottom() { return position.y - Constants.ROPE_CENTER.y; }
    @Override public final boolean isLedge() { return true; }
    @Override public Rope clone() { return new Rope(position); }
}
