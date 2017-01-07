package com.udacity.gamedev.gigagal.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Utils;

public class Ice implements Ground, Skateable {

    // fields
    private Vector2 position;
    private long startTime;

    // ctor
    public Ice(Vector2 position) {
        this.position = position;
        this.startTime = TimeUtils.nanoTime();
    }

    public void render(SpriteBatch batch) {
        final float elapsedTime = Utils.secondsSince(startTime);
        final TextureRegion region = Assets.getInstance().getIceAssets().ice.getKeyFrame(elapsedTime, true);
        Utils.drawTextureRegion(batch, region, position, Constants.ICE_CENTER);
    }

    public final Vector2 getPosition() { return position; }
    public final float getHeight() { return Constants.ICE_CENTER.x * 2; }
    public final float getWidth() { return Constants.ICE_CENTER.y * 2; }
    public final float getLeft() { return position.x - Constants.ICE_CENTER.x; }
    public final float getRight() { return position.x + Constants.ICE_CENTER.x; }
    public final float getTop() { return position.y + Constants.ICE_CENTER.y; }
    public final float getBottom() { return position.y - Constants.ICE_CENTER.y; }
    public final Class getSubclass() { return this.getClass(); }
}
