package com.udacity.gamedev.gigagal.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Helpers;

public class Ice implements Skateable, Ground {

    // fields
    private Vector2 position;
    private Vector2 scale;
    private Vector2 adjustedCenter;
    private long startTime;

    // ctor
    public Ice(Vector2 position, Vector2 scale, Vector2 adjustedCenter) {
        this.position = position;
        this.scale = scale;
        this.adjustedCenter = adjustedCenter;
        this.startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(SpriteBatch batch, Viewport viewport) {
        Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGroundAssets().ice.getKeyFrame(Helpers.secondsSince(startTime), true), position, adjustedCenter, scale);
    }

    @Override public final Vector2 getPosition() { return position; }
    @Override public final float getHeight() { return Constants.ICE_CENTER.x * 2 * scale.x; }
    @Override public final float getWidth() { return Constants.ICE_CENTER.y * 2 * scale.y; }
    @Override public final float getLeft() { return position.x - Constants.ICE_CENTER.x * scale.x; }
    @Override public final float getRight() { return position.x + Constants.ICE_CENTER.x * scale.x; }
    @Override public final float getTop() { return position.y + Constants.ICE_CENTER.y * scale.y; }
    @Override public final float getBottom() { return position.y - Constants.ICE_CENTER.y * scale.y; }
    @Override public Ice clone() { return new Ice(position, scale, adjustedCenter); }
}
