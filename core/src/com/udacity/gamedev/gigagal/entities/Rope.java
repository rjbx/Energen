package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Utils;

public class Rope implements Ground, Climbable {

    // fields
    private Vector2 position;

    // ctor
    public Rope(Vector2 position) {
        this.position = position;
    }

    public void render(SpriteBatch batch) {
        final TextureRegion region = Assets.getInstance().getRopeAssets().rope;
        Utils.drawTextureRegion(batch, region, position, Constants.ROPE_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.ROPE_CENTER.y * 2; }
    public final float getWidth() { return Constants.ROPE_CENTER.x * 2; }
    public final float getLeft() { return position.x - Constants.ROPE_CENTER.x; }
    public final float getRight() { return position.x + Constants.ROPE_CENTER.x; }
    public final float getTop() { return position.y + Constants.ROPE_CENTER.y; }
    public final float getBottom() { return position.y - Constants.ROPE_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}
